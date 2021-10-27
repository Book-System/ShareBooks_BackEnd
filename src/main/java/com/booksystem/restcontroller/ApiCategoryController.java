package com.booksystem.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booksystem.entity.Category;
import com.booksystem.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/category")
public class ApiCategoryController {

    @Autowired
    CategoryService cService;

    // 카테고리 등록(카테고리 코드, 카테고리 명)
    // POST > http://localhost:9090/REST/api/category/register
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> categoryRegisterPost(@ModelAttribute Category category) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // registerCategory메소드 호출
            int result = cService.registerCategory(category);

            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "카테고리 등록을 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "카테고리 등록을 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "카테고리 등록을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 카테고리 수정(카테고리 명)
    // PUT > http://localhost:9090/REST/api/category/update
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> categoryUpdatePut(@ModelAttribute Category category) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // updateCategory메소드 호출
            int result = cService.updateCategory(category);

            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "카테고리 수정을 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "카테고리 수정을 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "카테고리 수정을 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 카테고리 삭제
    // DELETE > http://localhost:9090/REST/api/category/remove
    @RequestMapping(value = "/remove", method = RequestMethod.DELETE, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> categoryRemoveDelete(@RequestParam Long categoryCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // deleteCategory메소드 호출
            int result = cService.deleteCategory(categoryCode);

            if (result == 1) {
                map.put("result", 1L);
                map.put("data", "카테고리 삭제를 성공했습니다.");
            } else {
                map.put("result", 0L);
                map.put("data", "카테고리 삭제를 실패했습니다.");
            }
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "카테고리 삭제를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 카테고리 목록 조회
    // GET > http://localhost:9090/REST/api/category/list
    @RequestMapping(value = "/list", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> categoryListGet() throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // listCategory메소드 호출
            List<Category> result = cService.listCategory();

            map.put("result", 1L);
            map.put("data", result);
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "카테고리 목록 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }

    // 카테고리 상세 조회
    // GET > http://localhost:9090/REST/api/category/detail
    @RequestMapping(value = "/detail", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> categoryDetailGet(@RequestParam Long categoryCode) throws Exception {
        Map<String, Object> map = new HashMap<>();
        try {
            // updateCategory메소드 호출
            Category result = cService.detailCategory(categoryCode);

            map.put("result", 1L);
            map.put("data", result);
        } catch (Exception e) {
            // 에러를 출력한다.
            e.printStackTrace();
            map.put("result", 0L);
            map.put("data", "카테고리 상세 조회를 실패했습니다.");
        }
        // 결과 값 리턴
        return map;
    }
}
