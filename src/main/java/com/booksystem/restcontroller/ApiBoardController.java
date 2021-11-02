package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Board;
import com.booksystem.entity.BoardImage;
import com.booksystem.entity.BoardReply;
import com.booksystem.entity.BoardSelect;
import com.booksystem.entity.BookReservation;
import com.booksystem.entity.Member;
import com.booksystem.entity.Reservation;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.repository.BookReservationRepository;
import com.booksystem.repository.ReservationRepository;
import com.booksystem.service.BoardImageService;
import com.booksystem.service.BoardReplyService;
import com.booksystem.service.BoardSelectService;
import com.booksystem.service.BoardService;
import com.booksystem.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
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
    BoardReplyService brService;

    @Autowired
    BoardService boService;

    @Autowired
    BoardImageService biService;

    @Autowired
    MemberService mService;

    @Autowired
    BoardSelectService bsService;

    // 얘 나중 삭제
    @Autowired
    ReservationRepository reservationRepository;

    // 얘 나중 삭제
    @Autowired
    BookReservationRepository bReservationRepository;

    @Autowired
    JwtUtil jwtUtil;

    // 고객센터 글쓰기
    // POST > http://localhost:9090/REST/api/board/insert
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardInsertPOST(@ModelAttribute Board board, @RequestHeader("token") String token,
            @RequestParam(name = "file", required = false) MultipartFile[] file,
            @RequestParam(name = "reservationNo", required = false) Long reservationNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 값을 사용하여 Member객체에서 추출
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);
            // board객체에 Member를 저장
            board.setMember(member);

            // reservation 객체 추출 및 저장
            if (reservationNo != null) {
                Reservation reservation = reservationRepository.getById(reservationNo);
                board.setReservation(reservation);
            }

            // DB에 저장
            Board result = boService.insertBoard(board);
            if (result != null && file != null) {
                // 이미지가 저장되었는지 판단하기 위한 변수
                int count = 0;
                for (int i = 0; i < file.length; i++) {
                    // 보드 번호 받아와서 insertimage수행
                    BoardImage boardImage = new BoardImage();
                    boardImage.setImage(file[i].getBytes());
                    boardImage.setImagename(file[i].getOriginalFilename());
                    boardImage.setImagesize(file[i].getSize());
                    boardImage.setImagetype(file[i].getContentType());
                    boardImage.setPriority(i + 1);
                    boardImage.setBoard(result);
                    biService.insertBoardImage(boardImage);
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
                // 이미지가 없는 글쓰기 성공
                map.put("result", 1L);
                map.put("data", "고객센터 글쓰기를 성공하였습니다.");
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

    // 고객센터 글목록 view
    // GET > http://localhost:9090/REST/api/board/select
    @RequestMapping(value = "/select", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardSelectTestGET() {
        Map<String, Object> map = new HashMap<>();
        try {
            List<BoardSelect> boardList = bsService.selectBoardView();
            map.put("result", 1L);
            map.put("boardList", boardList);
            System.out.println(boardList);
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
    // GET > http://localhost:9090/REST/api/board/select/one?boardNo=글번호
    @RequestMapping(value = "/select/one", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardSelectOneGET(@RequestParam("boardNo") Long boardNo) {
        Map<String, Object> map = new HashMap<>();

        try {
            // 번호에 해당하는 글 정보 가져오기
            // Board board = boService.selectContentBoard(boardNo).get();
            String board = boService.selectContentBoard(boardNo); // content만 추출 위에는 전부

            map.put("result", 1L);
            map.put("board", board);
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
    // http://localhost:9090/REST/api/board/select/image?boardNo=글번호&priority=우선순위
    // <img src="/board/select_one_image?boardNo=6&idx=0"/>
    @RequestMapping(value = "/select/image", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> boardSelectOneImagePreviewGET(@RequestParam("boardNo") Long boardNo,
            @RequestParam("priority") int priority) {
        try {
            // 글번호에 해당하는 이미지 리스트를 가져온다.
            List<BoardImage> boardImageList = biService.selectBoardImagePreview(boardNo);
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
    // DELETE > http://localhost:9090/REST/api/board/delete?boardNo=글번호
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardDeleteDELETE(@RequestParam("boardNo") Long boardNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != "" && !jwtUtil.isTokenExpired(token)) {
                // 이미지랑 답글을 먼저 삭제를 하고, 글을 삭제한다.
                biService.deleteBoardImage(boardNo);
                brService.deleteBoardReply(boardNo);
                boService.deleteBoard(boardNo);
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
    public Map<String, Object> boardUpdatePUT(@ModelAttribute Board board,
            @RequestParam(name = "boardimageNo") Long[] boardimageNo,
            @RequestParam(name = "file", required = false) MultipartFile[] file, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(board.getBoardNo());
        try {
            // Member객체 받아오기
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // board객체에 Member객체 설정
            board.setMember(member);
            boService.updateBoard(board);
            if (board != null && file != null) {
                int count = 0;
                for (int i = 0; i < file.length; i++) {
                    BoardImage boardImage = biService.findBoardImage(boardimageNo[i]);
                    boardImage.setImage(file[i].getBytes());
                    boardImage.setImagename(file[i].getOriginalFilename());
                    boardImage.setImagesize(file[i].getSize());
                    boardImage.setImagetype(file[i].getContentType());
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
            } else {
                // 이미지가 없는 글수정 성공
                map.put("result", 0L);
                map.put("data", "글 수정을 성공했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 수정을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 답글 쓰기
    // POST > http://localhost:9090/REST/api/board/insert/reply
    // {"content":"가나다","board":{"board_no":1}}
    @RequestMapping(value = "/insert/reply", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardInsertReplyPOST(@RequestBody BoardReply boardReply,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 값을 사용하여 Member객체에서 추출
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // board객체에 Member를 저장
            boardReply.setMember(member);

            // DB에 저장
            int result = brService.insertBoardReply(boardReply);
            map.put("result", result);

        } catch (Exception e) {
            // 에러를 출력
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "고객센터 글쓰기를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 답글 조회
    // GET > http://localhost:9090/REST/api/board/select/reply?boardNo=글번호
    @RequestMapping(value = "/select/reply", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardSelectReplyGET(@RequestParam("boardNo") Long boardNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 번호에 해당하는 글 정보 가져오기
            String boardReply = brService.selectBoardReply(boardNo);

            map.put("result", 1L);
            map.put("boardReply", boardReply);
            map.put("data", "글 상세 조회를 성공하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 상세 조회를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 답글 삭제
    // DELETE > http://localhost:9090/REST/api/board/delete/reply?boardNo=글번호
    @RequestMapping(value = "/delete/reply", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardDeleteReplyDELETE(@RequestParam("boardNo") Long boardNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != "" && !jwtUtil.isTokenExpired(token)) {
                // 이미지를 먼저 삭제를 하고, 글을 삭제한다.
                brService.deleteBoardReply(boardNo);
                map.put("result", 1L);
                map.put("data", "답글 삭제를 성공하였습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "답글 삭제를 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "답글 삭제를 실패하였습니다.");
        }
        return map;
    }

    // 고객센터 답글 수정
    // PUT > http://localhost:9090/REST/api/board/update/reply
    @RequestMapping(value = "/update/reply", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardUpdateReplyPUT(@RequestBody BoardReply boardReply,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // Member객체 받아오기
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // board객체에 Member객체 설정
            boardReply.setMember(member);
            brService.updateBoardReply(boardReply);
            System.out.println(boardReply.getContent());
            System.out.println(boardReply.getBoardreplyNo());
            map.put("result", 1L);
            map.put("data", "글 수정을 성공했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 수정을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 고객센터 답글 쓰기 전 갯수 체크
    // GET > http://localhost:9090/REST/api/board/check/reply?boardNo=글번호
    @RequestMapping(value = "/check/reply", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardCheckReplyGET(@RequestParam("boardNo") Long boardNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            int result = brService.checkBoardReply(boardNo);
            map.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
        }
        return map;
    }

    // 고객센터 이미지 갯수 체크
    // GET > http://localhost:9090/REST/api/board/check/image?boardNo=글번호
    @RequestMapping(value = "/check/image", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardCheckImageGET(@RequestParam("boardNo") Long boardNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            int result = biService.checkBoardImage(boardNo);
            map.put("result", result);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
        }
        return map;
    }

    // 고객센터 예약목록 조회
    // GET > http://localhost:9090/REST/api/board/reservation/list
    @RequestMapping(value = "/reservation/list", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> boardReservationListGET(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = jwtUtil.extractUsername(token);
            System.out.println(memberId);

            List<BookReservation> list = bReservationRepository.findByMemberId(memberId);
            map.put("result", 1L);
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
        }
        return map;
    }
}
