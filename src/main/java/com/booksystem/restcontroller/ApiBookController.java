package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookImage;
import com.booksystem.entity.BookProjection;
import com.booksystem.entity.Category;
import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.BookImageService;
import com.booksystem.service.BookService;
import com.booksystem.service.CategoryService;
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
@RequestMapping(value = "/api/book")
public class ApiBookController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    BookService bService;

    @Autowired
    BookImageService bookImageService;

    @Autowired
    MemberService mService;

    @Autowired
    CategoryService cService;

    // 책 등록
    // POST > http://localhost:9090/REST/api/book/register
    @RequestMapping(value = "/register", method = {
            RequestMethod.POST }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> bookRegisterPost(@ModelAttribute Book book,
            @RequestParam(name = "categoryCode") Long categoryCode, @RequestParam(name = "file") MultipartFile[] file,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 토큰 값을 이용하여, 아이디를 추출
                String memberId = jwtUtil.extractUsername(token);
                // member_id를 조건으로 member객체 추출
                Member member = mService.getMember(memberId);
                // categoryCode를 조건으로 category객체 추출
                Category category = cService.detailCategory(categoryCode);

                // Member객체와 Category객체 설정
                book.setMember(member);
                book.setCategory(category);

                // registerBook메소드를 호출
                Book result = bService.registerBook(book);

                // 이미지가 1개 이상 들어왔을 경우
                if (file.length > 0) {
                    int count = 0;
                    // 파일로 들어온 이미지 수량만큼 저장
                    for (int i = 0; i < file.length; i++) {
                        BookImage bookImage = new BookImage();
                        bookImage.setOriginalname(file[i].getOriginalFilename());
                        bookImage.setFiletype(file[i].getContentType());
                        bookImage.setFilesize(file[i].getSize());
                        bookImage.setFiledata(file[i].getBytes());
                        bookImage.setPriority(i + 1);
                        bookImage.setBook(result);
                        bookImageService.registerBookImage(bookImage);
                        count++;
                    }

                    if (count == file.length) {
                        map.put("result", 1L);
                        map.put("data", "책 등록을 성공했습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "책 등록을 실패했습니다.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("result", "책 등록을 실패했습니다.");
        }
        return map;
    }

    // 책 수정
    // PUT > http://localhost:9090/REST/api/book/update
    @RequestMapping(value = "/update", method = {
            RequestMethod.PUT }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateBookPut(@ModelAttribute Book book,
            @RequestParam(name = "categoryCode") Long categoryCode,
            @RequestParam(name = "bookimageNo") Long[] bookimageNo,
            @RequestParam(name = "file", required = false) MultipartFile[] file, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                Member member = mService.getMember(memberId);
                Category category = cService.detailCategory(categoryCode);

                // Member객체와 Category객체 설정
                book.setMember(member);
                book.setCategory(category);

                // updateBook메소드 호출
                Book result = bService.updateBook(book);

                // 파일이 한 개 이상일 경우
                if (result != null) {
                    int count = 0;
                    // 파일로 들어온 이미지 수량만큼 저장
                    for (int i = 0; i < file.length; i++) {
                        BookImage bookImage = bookImageService.getBookImage(bookimageNo[i]);
                        bookImage.setOriginalname(file[i].getOriginalFilename());
                        bookImage.setFiletype(file[i].getContentType());
                        bookImage.setFilesize(file[i].getSize());
                        bookImage.setFiledata(file[i].getBytes());
                        bookImageService.updateBookImage(bookImage);
                        count++;
                    }

                    if (count == file.length) {
                        map.put("result", 1L);
                        map.put("data", "책 수정을 성공했습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "책 수정을 실패했습니다.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 수정을 실패했습니다.");
        }
        return map;
    }

    // 책 삭제
    // DELETE > http://localhost:9090/REST/api/book/delete
    @RequestMapping(value = "/delete", method = {
            RequestMethod.DELETE }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> deleteBookDelete(@RequestParam(name = "bookNo") Long bookNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // deleteBookImage메소드, deleteBook메소드 호출
                bookImageService.deleteBookImage(bookNo);
                bService.deleteBook(bookNo);
                map.put("result", 1L);
                map.put("data", "책 삭제를 성공했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 삭제를 실패했습니다.");
        }
        return map;
    }

    // 책 목록 조회 => 이미지도 함께 조회할 방법을 생각
    // GET > http://localhost:9090/REST/api/book/list
    @RequestMapping(value = "/list", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listBookGet() {
        Map<String, Object> map = new HashMap<>();
        try {
            // listBook메소드 호출
            List<BookProjection> list = bService.listBook();
            map.put("result", 1L);
            map.put("data", list);
        } catch (

        Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 목록 조회를 실패했습니다.");
        }
        return map;
    }

    // 책 상세 조회
    // GET > http://localhost:9090/REST/api/book/detail?bookno=?
    @RequestMapping(value = "/detail", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> detailBookGet(@RequestParam("bookno") Long bookNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            // listBook메소드 호출
            // native
            // Book book = bService.detailBook(bookNo);

            // jpa
            Book book = bService.detailBookJPA(bookNo);
            map.put("result", 1L);
            map.put("data", book);
        } catch (

        Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 목록 조회를 실패했습니다.");
        }
        return map;
    }

    // 책 이미지 조회
    // GET > http://localhost:9090/REST/api/book/image?bookno=책번호&priority=우선순위
    @RequestMapping(value = "/image", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> BookImageGet(@RequestParam("bookno") Long bookNo,
            @RequestParam("priority") int priority) {
        try {
            // 글 번호에 해당하는 이미지 리스트를 가져온다.
            List<BookImage> bookImageList = bookImageService.listBookImage(bookNo);

            // 우선순위에 해당하는 이미지를 가져온다.
            BookImage bookImage = bookImageList.get(priority);
            HttpHeaders headers = new HttpHeaders();
            if (bookImage.getFiletype().equals("image/jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (bookImage.getFiletype().equals("image/png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (bookImage.getFiletype().equals("image/gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            }
            ResponseEntity<byte[]> response = new ResponseEntity<>(bookImage.getFiledata(), headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 책 이미지 개수 조회
    // GET > http://localhost:9090/REST/api/book/image/count?bookno=책번호
    @RequestMapping(value = "/image/count", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookImageCountGet(@RequestParam("bookno") Long bookNo) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 글 번호에 해당하는 이미지 리스트를 가져온다.
            int bookImageCount = bookImageService.countBookImage(bookNo);

            map.put("result", bookImageCount);
            map.put("data", "이미지 개수 조회를 성공하였습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "이미지 개수 조회를 실패하였습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}
