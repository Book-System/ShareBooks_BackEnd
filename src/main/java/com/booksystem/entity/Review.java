package com.booksystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "REVIEW")
@SequenceGenerator(name = "SEQ_REVIEW_NO", sequenceName = "SEQ_REVIEWN_NO", initialValue = 1, allocationSize = 1)
public class Review {

    @Id
    @Column(name = "REVIEW_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REVIEW_NO")
    private Long reviewNo = 0L;

    @Column(name = "RATING")
    private int rating = 0;

    @Column(name = "CONTENT")
    private String content = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BOOK_NO")
    private Book book;
}
