package com.booksystem.repository;

import java.util.List;

import com.booksystem.entity.BoardSelect;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardSelectRepository extends JpaRepository<BoardSelect, Long> {
    // 고객센터 글목록
    @Query(value = "SELECT * FROM BOARD_SELECT1 ORDER BY BOARD_NO DESC", nativeQuery = true)
    public List<BoardSelect> querySelectBoardView();
}
