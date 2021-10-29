package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Board;
import com.booksystem.entity.BoardImage;
import com.booksystem.entity.BoardImageProjection;
import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.BoardImageService;
import com.booksystem.service.BoardService;
import com.booksystem.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/board")
public class ApiBoardController {

    @Autowired
    BoardService boService;

    @Autowired
    BoardImageService biService;

    @Autowired
    MemberService mService;

    @Autowired
    JwtUtil jwtUtil;

    // 고객센터 글쓰기
    // POST > http://localhost:9090/REST/api/board/insert
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardInsertPOST(@ModelAttribute Board board, @RequestHeader("token") String token,
            @RequestParam(name = "file") MultipartFile[] file) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 값을 사용하여 Member객체에서 추출
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // board객체에 Member를 저장
            board.setMember(member);

            // DB에 저장
            Board result = boService.insertBoard(board);
            if (result != null) {
                // 이미지가 저장되었는지 판단하기 위한 변수
                int count = 0;
                for (int i = 0; i < file.length; i++) {
                    // 보드 번호 받아와서 insertimage수행
                    BoardImage boardimage = new BoardImage();
                    boardimage.setImage(file[i].getBytes());
                    boardimage.setImagename(file[i].getOriginalFilename());
                    boardimage.setImagesize(file[i].getSize());
                    boardimage.setImagetype(file[i].getContentType());
                    boardimage.setPriority(i + 1);
                    boardimage.setBoard(result);
                    biService.insertBoardImage(boardimage);
                    count++;
                }

                // count의 값과 file.length의 값이 같을 경우 등록성공
                if (count == file.length) {
                    map.put("result", 1L);
                    map.put("data", "고객센터 글쓰기를 성공하였습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "고객센터 글쓰기를 실패하였습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "고객센터 글쓰기를 실패하였습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "고객센터 글쓰기를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 글목록
    // GET > http://localhost:9090/REST/api/board/select
    @RequestMapping(value = "/select", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardSelectGET() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Board> boardList = boService.selectBoard();
            map.put("result", 1L);
            map.put("boardList", boardList);
            map.put("data", "글 목록 조회를 성공하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 목록 조회를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 상세페이지
    // GET > http://localhost:9090/REST/api/board/select/one?no=글번호
    @RequestMapping(value = "/select/one", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardSelectOneGET(@RequestParam("no") Long no) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 번호에 해당하는 글 정보 가져오기
            Board board = boService.selectOneBoard(no);

            // 프로젝션으로 필요한 항목만 추출하기
            List<BoardImageProjection> boardImageList = biService.selectBoardImage(no);

            map.put("result", 1L);
            map.put("board", board);
            map.put("boardImageList", boardImageList);
            map.put("data", "글 상세 조회를 성공하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 상세 조회를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 상세페이지 이미지
    // GET >
    // http://localhost:9090/REST/api/board/select/image?boardno=글번호&priority=우선순위
    // <img src="/board/select_one_image?boardno=6&idx=0"/>
    @RequestMapping(value = "/select/image", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> boardSelectOneImagePreviewGET(@RequestParam("boardno") Long boardno,
            @RequestParam("priority") int priority) {
        try {
            // 글 번호에 해당하는 이미지 리스트를 가져온다.
            List<BoardImage> boardImageList = biService.selectBoardImagePreview(boardno);

            // 우선순위에 해당하는 이미지를 가져온다.
            BoardImage boardImage = boardImageList.get(priority);
            HttpHeaders headers = new HttpHeaders();
            if (boardImage.getImagetype().equals("image/jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (boardImage.getImagetype().equals("image/png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (boardImage.getImagetype().equals("image/gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            }
            ResponseEntity<byte[]> response = new ResponseEntity<>(boardImage.getImage(), headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 고객센터 글삭제
    // DELETE > http://localhost:9090/REST/api/board/delete
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardDeleteDELETE(@RequestParam("no") Long no, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != "" && !jwtUtil.isTokenExpired(token)) {
                // 이미지를 먼저 삭제를 하고, 글을 삭제한다.
                biService.deleteBoardImage(no);
                boService.deleteBoard(no);
                map.put("result", 1L);
                map.put("data", "글 삭제를 성공하였습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "글 삭제를 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 삭제를 실패하였습니다.");
        }
        return map;
    }

    // 고객센터 글수정
    // PUT > http://localhost:9090/REST/api/board/update
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardUpdatePUT(@ModelAttribute Board board, @ModelAttribute BoardImage boardImage,
            @RequestParam(name = "file") MultipartFile[] file, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // Member객체 받아오기
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // board객체에 Member객체 설정
            board.setMember(member);
            boService.updateBoard(board);
            if (board != null) {
                int count = 0;
                for (int i = 0; i < file.length; i++) {
                    boardImage.setImage(file[i].getBytes());
                    boardImage.setImagename(file[i].getOriginalFilename());
                    boardImage.setImagesize(file[i].getSize());
                    boardImage.setImagetype(file[i].getContentType());
                    boardImage.setPriority(i + 1);
                    biService.updateBoardImage(boardImage);
                    count++;
                }
                if (count == file.length) {
                    map.put("result", 1L);
                    map.put("data", "글 수정을 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "글 수정을 실패했습니다.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 수정을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}
