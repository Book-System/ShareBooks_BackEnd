package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.booksystem.dto.SnsDTO;
import com.booksystem.dto.SnsGoodDTO;
import com.booksystem.dto.SnsImageDTO;
import com.booksystem.dto.SnsReplyDTO;
import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.MemberService;
import com.booksystem.service.SnsGoodService;
import com.booksystem.service.SnsImageService;
import com.booksystem.service.SnsReplyService;
import com.booksystem.service.SnsService;

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
@RequestMapping(value = "/api/sns")
public class ApiSnsController {

    @Autowired
    SnsService sService;

    @Autowired
    MemberService mService;

    @Autowired
    SnsImageService siService;

    @Autowired
    SnsReplyService srService;

    @Autowired
    SnsGoodService sgService;

    @Autowired
    JwtUtil jwtUtil;

    // 우리동네 글쓰기
    // POST > http://localhost:9090/REST/api/sns/insert
    @RequestMapping(value = "/insert", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> insertPost(@ModelAttribute SnsDTO sns,
            @RequestParam(name = "file", required = false) MultipartFile[] file, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 값을 사용하여 Member객체에서 추출
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            sns.setMember(member.getId());
            sns.setAddress(member.getAddress());
            sns.setMemberName(member.getNickname());
            sService.insertSns(sns);

            if (sns != null && file != null) {
                // 이미지가 저장되었는지 판단하기 위한 변수
                int count = 0;
                for (int i = 0; i < file.length; i++) {
                    // 보드 번호 받아와서 insertimage수행
                    SnsImageDTO snsimage = new SnsImageDTO();
                    snsimage.setImage(file[i].getBytes());
                    snsimage.setImagename(file[i].getOriginalFilename());
                    snsimage.setImagesize(file[i].getSize());
                    snsimage.setImagetype(file[i].getContentType());
                    snsimage.setPriority(i + 1);
                    snsimage.setSnsNo(sns.getSnsNo());
                    siService.insertSnsImage(snsimage);
                    count++;
                }

                // count의 값과 file.length의 값이 같을 경우 등록성공
                if (count == file.length) {
                    map.put("result", 1L);
                    map.put("data", "우리동네 글쓰기를 성공하였습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "우리동네 글쓰기를 실패하였습니다.");
                }
            } else {
                // 이미지가 없는 글쓰기 성공
                map.put("result", 1L);
                map.put("data", "(이미지X)우리동네 글쓰기를 성공하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("stauts", e.hashCode());
        }
        return map;
    }

    // 우리동네 글목록
    // GET > http://localhost:9090/REST/api/sns/select/all
    @RequestMapping(value = "/select/all", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectAllGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = jwtUtil.extractUsername(token);

            List<SnsDTO> sns = sService.selectSnsAll(memberId);
            map.put("status", 200);
            map.put("sns", sns);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 우리동네 글수정 시 글목록
    // GET > http://localhost:9090/REST/api/sns/select?snsNo=글번호
    @RequestMapping(value = "/select", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectGet(@RequestParam("snsNo") Long snsNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            Optional<SnsDTO> sns = sService.selectSns(snsNo);
            map.put("status", 200);
            map.put("sns", sns);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // 우리동네 이미지
    // GET >
    // http://localhost:9090/REST/api/sns/select/image?snsNo=글번호&priority=우선순위
    // <img src="/sns/select/image?snsNo=6&priority=0"/>
    @RequestMapping(value = "/select/image", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> boardSelectOneImagePreviewGET(@RequestParam("snsNo") Long snsNo,
            @RequestParam("priority") int priority) {
        try {
            // 글번호에 해당하는 이미지 리스트를 가져온다.
            List<SnsImageDTO> snsImageList = siService.selectSnsImage(snsNo);
            // 우선순위에 해당하는 이미지를 가져온다.
            SnsImageDTO boardImage = snsImageList.get(priority);
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

    // 우리동네 글삭제
    // DELETE > http://localhost:9090/REST/api/sns/delete?snsNo=글번호
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteDelete(@RequestParam("snsNo") Long snsNo, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != "" && !jwtUtil.isTokenExpired(token)) {

                sgService.deleteGoodsSns(snsNo);
                srService.deleteRepliesSns(snsNo);
                siService.deleteImageSns(snsNo);
                sService.deleteSns(snsNo);

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

    // 우리동네 글수정
    // PUT > http://localhost:9090/REST/api/sns/update
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updatePut(@ModelAttribute SnsDTO sns,
            @RequestParam(name = "priority", required = false) Long[] priority,
            @RequestParam(name = "file", required = false) MultipartFile[] file, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            sns.setMember(member.getId());
            sService.updateSns(sns);
            System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + sns.getSnsNo());
            if (sns != null && file != null) {
                int count = 0;
                for (int i = 0; i < file.length; i++) {
                    SnsImageDTO snsimage = siService.findSnsImage(sns.getSnsNo(),
                            Integer.parseInt(String.valueOf(priority[i])));
                    snsimage.setImage(file[i].getBytes());
                    snsimage.setImagename(file[i].getOriginalFilename());
                    snsimage.setImagesize(file[i].getSize());
                    snsimage.setImagetype(file[i].getContentType());
                    siService.updateImageSns(snsimage);
                    System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + snsimage.getSnsNo());
                    System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + snsimage.getPriority());
                    System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + snsimage.getImagesize());
                    System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + snsimage.getImagename());
                    System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + snsimage.getImagetype());
                    System.out.println("pleaseeeeeeeeeeeeeeeeeeeeee" + snsimage.getSnsimageNo());
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
                map.put("result", 1L);
                map.put("data", "(이미지X)글 수정을 성공했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "글 수정을 실패하였습니다.");
        }
        return map;
    }

    // 우리동네 댓글 쓰기
    // POST > http://localhost:9090/REST/api/sns/insert/reply
    // {"content":"가나다","snsNo":1}
    @RequestMapping(value = "/insert/reply", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> snsInsertReplyPOST(@RequestBody SnsReplyDTO snsreply,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 토큰 값을 사용하여 Member객체에서 추출
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // snsreply객체에 Member를 저장
            snsreply.setMember(member.getId());
            snsreply.setMemberName(member.getNickname());

            // DB에 저장
            srService.insertReplySns(snsreply);
            map.put("result", 1L);
            map.put("data", "댓글쓰기를 성공하였습니다");

        } catch (Exception e) {
            // 에러를 출력
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "댓글쓰기를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 우리동네 댓글 조회
    // GET > http://localhost:9090/REST/api/sns/select/reply?snsNo=글번호
    @RequestMapping(value = "/select/reply", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> snsSelectReplyGET(@RequestParam("snsNo") Long snsNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 번호에 해당하는 글 정보 가져오기
            List<SnsReplyDTO> snsReply = srService.selectReplySns(snsNo);

            map.put("result", 1L);
            map.put("snsReply", snsReply);
            map.put("data", "댓글 조회를 성공하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "댓글 조회를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 우리동네 댓글 삭제
    // DELETE > http://localhost:9090/REST/api/sns/delete/reply?snsreplyNo=댓글번호
    @RequestMapping(value = "/delete/reply", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> snsDeleteReplyDELETE(@RequestParam("snsreplyNo") Long snsreplyNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != "" && !jwtUtil.isTokenExpired(token)) {
                srService.deleteReplySns(snsreplyNo);
                map.put("result", 1L);
                map.put("data", "댓글 삭제를 성공하였습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "댓글 삭제를 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "댓글 삭제를 실패하였습니다.");
        }
        return map;
    }

    // 우리동네 댓글 수정
    // PUT > http://localhost:9090/REST/api/sns/update/reply
    @RequestMapping(value = "/update/reply", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> snsUpdateReplyPUT(@RequestBody SnsReplyDTO snsreply,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // Member객체 받아오기
            String memberId = jwtUtil.extractUsername(token);
            Member member = mService.getMember(memberId);

            // board객체에 Member객체 설정
            snsreply.setMember(member.getId());
            srService.updateReplySns(snsreply);
            map.put("result", 1L);
            map.put("data", "댓글 수정을 성공했습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "댓글 수정을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 우리동네 좋아요(내가 했는지 안했는지)
    // GET > http://localhost:9090/REST/api/sns/check/good?snsNo=글번호
    @RequestMapping(value = "/check/good", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> snsCheckGoodGET(@RequestParam(name = "snsNo") Long snsNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (token != "" && !jwtUtil.isTokenExpired(token)) {
                String memberId = jwtUtil.extractUsername(token);
                System.out.println(memberId);
                System.out.println(snsNo);
                int result = sgService.checkGoodSns(snsNo, memberId);
                System.out.println(result);
                if (result != 1) {
                    // 토큰 값을 사용하여 Member객체에서 추출
                    Member member = mService.getMember(memberId);

                    SnsGoodDTO snsgood = new SnsGoodDTO();

                    snsgood.setMember(member.getId());
                    snsgood.setSnsNo(snsNo);
                    // DB에 저장
                    result = sgService.insertGoodSns(snsgood);
                    if (result == 1) {
                        map.put("result", 1L);
                        map.put("data", "좋아요를 성공하였습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "좋아요를 실패하였습니다.");
                    }
                } else {
                    result = sgService.deleteGoodSns(snsNo, memberId);
                    if (result == 1) {
                        map.put("result", 1L);
                        map.put("data", "좋아요 취소를 성공하였습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "좋아요 취소를 실패하였습니다.");
                    }
                }
            } else {
                map.put("result", 0L);
                map.put("data", "좋아요 조회를 실패하였습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
        }
        return map;
    }

    // 우리동네 글수정 시 이미지 수정 이미지 번호 받아오기
    // GET > http://localhost:9090/REST/api/sns/image/priority
    @RequestMapping(value = "/image/priority", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> ImagePriorityGet(@RequestParam("snsNo") Long snsNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<SnsImageDTO> sns = siService.findSnsImagePriority(snsNo);
            map.put("status", 200);
            map.put("sns", sns);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }

    // ------------------------------------------------------------------------------------------------------------------------------
    // 우리동네 글목록
    // GET > http://localhost:9090/REST/api/sns/select/good
    @RequestMapping(value = "/select/good", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> selectGoodGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            String memberId = jwtUtil.extractUsername(token);

            List<SnsDTO> sns = sService.selectSnsGood(memberId);
            map.put("status", 200);
            map.put("sns", sns);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", e.hashCode());
        }
        return map;
    }
}
