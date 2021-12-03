package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Book;
import com.booksystem.entity.BookImage;
import com.booksystem.entity.BookLike;
import com.booksystem.entity.BookLikeProjection;
import com.booksystem.entity.BookProjection;
import com.booksystem.entity.Category;
import com.booksystem.entity.Member;
import com.booksystem.jwt.JwtUtil;
import com.booksystem.service.BookImageService;
import com.booksystem.service.BookLikeService;
import com.booksystem.service.BookService;
import com.booksystem.service.CategoryService;
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
@RequestMapping(value = "/api/book")
public class ApiBookController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    BookService bookService;

    @Autowired
    BookImageService bookImageService;

    @Autowired
    BookLikeService bookLikeService;

    @Autowired
    MemberService memberService;

    @Autowired
    CategoryService categoryService;

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
                // memberId를 조건으로 member객체 추출
                Member member = memberService.getMember(memberId);
                // categoryCode를 조건으로 category객체 추출
                Category category = categoryService.detailCategory(categoryCode);

                // Member객체와 Category객체 설정
                book.setMember(member);
                book.setCategory(category);

                // registerBook메소드를 호출
                Book result = bookService.registerBook(book);

                // 이미지가 1개 이상 들어왔을 경우
                if (file.length > 0) {
                    int count = 0;
                    // 파일로 들어온 이미지 수량만큼 저장
                    for (int i = 0; i < file.length; i++) {
                        BookImage bookImage = new BookImage();
                        bookImage.setImagename(file[i].getOriginalFilename());
                        bookImage.setImagetype(file[i].getContentType());
                        bookImage.setImagesize(file[i].getSize());
                        bookImage.setImage(file[i].getBytes());
                        bookImage.setPriority(i + 1);
                        bookImage.setBook(result);
                        bookImageService.registerBookImage(bookImage);
                        count++;
                    }

                    // 등록된 개수가 파일의 길이와 같을 경우 성공
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
            @RequestParam(name = "priority", required = false) Long[] priority,
            @RequestParam(name = "file", required = false) MultipartFile[] file, @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                System.out.println(book.toString());

                // updateBook메소드 호출
                int result = bookService.updateBook(book);
                System.out.println("결과 값" + result);
                System.out.println(file == null);

                // 책에 대한 정보가 저장되었을 경우
                if (result == 1 && file != null) {
                    int count = 0;
                    // 파일로 들어온 이미지 수량만큼 저장
                    for (int i = 0; i < file.length; i++) {
                        System.out.println(file[i].getOriginalFilename());
                        System.out.println(priority[i]);
                        BookImage bookImage = bookImageService.mainBookImage(book.getBookNo(),
                                Integer.parseInt(String.valueOf(priority[i])));
                        bookImage.setImagename(file[i].getOriginalFilename());
                        bookImage.setImagetype(file[i].getContentType());
                        bookImage.setImagesize(file[i].getSize());
                        bookImage.setImage(file[i].getBytes());
                        bookImageService.updateBookImage(bookImage);
                        count++;
                    }
                    if (count == file.length) {
                        map.put("result", 1L);
                        map.put("data", "책 수정(이미지포함)을 성공했습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "책 수정을 실패했습니다.");
                    }
                } else if (result == 1 && file == null) {
                    map.put("result", 1L);
                    map.put("data", "책 수정(이미지없음)을 성공했습니다.");
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
    // DELETE > http://localhost:9090/REST/api/book/remove
    @RequestMapping(value = "/remove", method = {
            RequestMethod.DELETE }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> removeBookDelete(@RequestBody Map<String, Object> obj,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                Long bookNo = Long.parseLong(String.valueOf(obj.get("bookNo")));

                // deleteBookImage메소드, deleteBook메소드 호출
                bookImageService.deleteBookImage(bookNo);
                bookService.deleteBook(bookNo);
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

    // 책 목록 조회
    // GET > http://localhost:9090/REST/api/book/list
    @RequestMapping(value = "/list", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listBookGet() {
        Map<String, Object> map = new HashMap<>();
        try {
            // listBook메소드 호출
            List<BookProjection> listBook = bookService.listBook();
            map.put("result", 1L);
            map.put("data", listBook);

        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 목록 조회를 실패했습니다.");
        }
        return map;
    }

    // 주소로 책목록 구하기 (페이지네이션)
    // GET > http://localhost:9090/REST/api/book/list/search?address=지역명&page=페이지
    @RequestMapping(value = "/list/search", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listAddressBookPageGet(@RequestParam(name = "address") String address,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<BookProjection> list = bookService.listSearchBook(address, page);
            map.put("result", 1L);
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 목록 조회를 실패했습니다.");
        }
        return map;
    }

    // 책 개수 조회
    // GET > http://localhost:9090/REST/api/book/count
    @RequestMapping(value = "/count", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> countBookGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            // 로그인된 사용자를 검증
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // 토큰 값을 사용하여 아이디 추출
                String memberId = jwtUtil.extractUsername(token);

                // 자신이 빌린 책들의 정보를 조회
                int rendBookCount = bookService.countBook(memberId);
                map.put("result", 1L);
                map.put("data", rendBookCount);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "빌린 책 조회를 실패했습니다.");
        }
        return map;
    }

    // 등록한 책 목록 조회
    // GET > http://localhost:9090/REST/api/book/list/rend
    @RequestMapping(value = "/list/rend", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listRendBookGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                List<BookProjection> listRendBook = bookService.listRendBook(memberId);
                map.put("result", 1L);
                map.put("data", listRendBook);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
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
            BookProjection book = bookService.detailBookProjection(bookNo);
            map.put("result", 1L);
            map.put("data", book);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 상세 조회를 실패했습니다.");
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
            if (bookImage.getImagetype().equals("image/jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (bookImage.getImagetype().equals("image/png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (bookImage.getImagetype().equals("image/gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            }
            ResponseEntity<byte[]> response = new ResponseEntity<>(bookImage.getImage(), headers, HttpStatus.OK);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 책 대표 이미지 조회
    // GET > http://localhost:9090/REST/api/book/image/main?bookno=책번호&priority=우선순위
    @RequestMapping(value = "/image/main", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> BookImageMainGet(@RequestParam("bookno") Long bookNo,
            @RequestParam("priority") int priority) {
        try {
            // 우선순위에 해당하는 이미지를 가져온다.
            BookImage bookImage = bookImageService.mainBookImage(bookNo, priority);
            HttpHeaders headers = new HttpHeaders();
            if (bookImage.getImagetype().equals("image/jpeg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (bookImage.getImagetype().equals("image/png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            } else if (bookImage.getImagetype().equals("image/gif")) {
                headers.setContentType(MediaType.IMAGE_GIF);
            }
            ResponseEntity<byte[]> response = new ResponseEntity<>(bookImage.getImage(), headers, HttpStatus.OK);
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

    // 책 찜 등록
    // POST > http://localhost:9090/REST/api/book/like/register
    @RequestMapping(value = "/like/register", method = {
            RequestMethod.POST }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookLikeRegisterPost(@RequestParam(name = "bookNo") Long bookNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // member객체와 book객체를 가져온다.
                String memberId = jwtUtil.extractUsername(token);
                Member member = memberService.getMember(memberId);
                Book book = bookService.detailBook(bookNo);

                // bookLike객체에 set메소드를 통해 저장
                BookLike bookLike = new BookLike();
                bookLike.setBook(book);
                bookLike.setMember(member);

                // registerBookLike메소드 호출
                int result = bookLikeService.registerBookLike(bookLike);

                if (result == 1) {
                    map.put("result", 1L);
                    map.put("data", "책 찜을 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 찜을 실패했습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 찜 등록
    // POST > http://localhost:9090/REST/api/book/like/register
    @RequestMapping(value = "/like/register2", method = {
            RequestMethod.POST }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookLikeRegister2Post(@RequestParam(name = "bookNo") Long bookNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // member객체와 book객체를 가져온다.
                String memberId = jwtUtil.extractUsername(token);
                Member member = memberService.getMember(memberId);
                Book book = bookService.detailBook(bookNo);

                BookLikeProjection count = bookLikeService.detailBookLike(member, book);

                // 해당 품목이 찜이 안되어있을 경우
                if (count == null) {
                    // bookLike객체에 set메소드를 통해 저장
                    BookLike bookLike = new BookLike();
                    bookLike.setBook(book);
                    bookLike.setMember(member);

                    // registerBookLike메소드 호출
                    int result = bookLikeService.registerBookLike(bookLike);

                    if ((int) result == 1) {
                        map.put("result", 1L);
                        map.put("data", "책 찜을 성공했습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "책 찜을 실패했습니다.");
                    }
                }
                // 해당 품목이 찜이 되어있을 경우
                else if (count != null) {
                    int result = bookLikeService.removeBookLike2(book, member);

                    if ((int) result == 1) {
                        map.put("result", 1L);
                        map.put("data", "책 찜 해제를 성공했습니다.");
                    } else {
                        map.put("result", 0L);
                        map.put("data", "책 찜 해제를 실패했습니다.");
                    }
                }

            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 찜 해제
    // DELETE > http://localhost:9090/REST/api/book/like/remove
    @RequestMapping(value = "/like/remove", method = {
            RequestMethod.DELETE }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookLikeRemoveDelete(@RequestParam("booklikeno") Long booklikeNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // removeBookLike메소드 호출
                int result = bookLikeService.removeBookLike(booklikeNo);

                if (result == 1) {
                    map.put("result", 1L);
                    map.put("data", "책 찜 해제를 성공했습니다.");
                } else {
                    map.put("result", 0L);
                    map.put("data", "책 찜 해제를 실패했습니다.");
                }
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜 해제를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 찜 개수 조회
    // GET > http://localhost:9090/REST/api/book/like/count
    @RequestMapping(value = "/like/count", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookLikeCountGet(@RequestParam("bookno") Long bookNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                Member member = memberService.getMember(memberId);
                Book book = bookService.detailBook(bookNo);

                // removeBookLike메소드 호출
                Object result = bookLikeService.countBookLike(member, book);

                map.put("result", (int) result);
                map.put("data", "책 찜 개수 조회를 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜 개수 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 찜 목록 조회
    // GET > http://localhost:9090/REST/api/book/like/list
    @RequestMapping(value = "/like/list", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookLikeListGet(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                Member member = memberService.getMember(memberId);

                // removeBookLike메소드 호출
                List<BookLikeProjection> bookLikeList = bookLikeService.listBookLike(member);

                map.put("result", 1L);
                map.put("data", bookLikeList);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 찜 상세 조회
    // GET > http://localhost:9090/REST/api/book/like/detail
    @RequestMapping(value = "/like/detail", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookLikeDetailGet(@RequestParam("bookno") Long bookNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                String memberId = jwtUtil.extractUsername(token);
                Member member = memberService.getMember(memberId);
                Book book = bookService.detailBook(bookNo);

                // removeBookLike메소드 호출
                BookLikeProjection bookLike = bookLikeService.detailBookLike(member, book);

                map.put("result", 1L);
                map.put("data", bookLike);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜 상세 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 이미지 목록 조회
    // GET > http://localhost:9090/REST/api/book/image/list?bookno=책번호
    @RequestMapping(value = "/image/list", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> BookImageListGet(@RequestParam("bookno") Long bookNo,
            @RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!jwtUtil.isTokenExpired(token) && !token.isEmpty()) {
                // removeBookLike메소드 호출
                List<BookImage> bookImage = bookImageService.listBookImage(bookNo);

                map.put("result", 1L);
                map.put("data", bookImage);
            } else {
                map.put("result", 0L);
                map.put("data", "로그인 인증을 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 찜 상세 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 책 목록 조회(주소)
    // GET > http://localhost:9090/REST/api/book/list/total?address=지역명
    @RequestMapping(value = "/list/total", method = {
            RequestMethod.GET }, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> listAddressBookGet(@RequestParam(name = "address") String address) {
        Map<String, Object> map = new HashMap<>();
        try {
            // listBook메소드 호출
            List<BookProjection> listAddressBook = bookService.listAddressBook(address);
            map.put("result", 1L);
            map.put("data", listAddressBook);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "책 목록 조회를 실패했습니다.");
        }
        return map;
    }
}