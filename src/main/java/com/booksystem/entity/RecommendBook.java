package com.booksystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Immutable
@Table(name = "RECOMMEND_BEST_BOOK_VIEW")
public class RecommendBook {
    @Id
    @Column(name = "BOOK_NO")
    private Long bookNo = 0L;

    @Column(name = "RATING")
    private int rating = 0;

    @Column(name = "CATEGORY_CODE")
    private Long categoryCode = 0L;

    @Column(name = "BORROW")
    private Long borrow = 0L;
}
