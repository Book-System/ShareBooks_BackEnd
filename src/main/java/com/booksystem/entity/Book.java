package com.booksystem.entity;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Lob;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "BOOK")
@SequenceGenerator(name = "SEQ_BOOK_NO", sequenceName = "SEQ_BOOK_NO", initialValue = 1, allocationSize = 1)
public class Book {

    @Id
    @Column(name = "BOOK_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOK_NO")
    private Long bookNo = 0L;

    @Column(name = "TITLE")
    private String title = null;

    @Lob
    @Column(name = "CONTENT")
    private String content = null;

    @Column(name = "PRICE")
    private Long price = 0L;

    @Column(name = "ADDRESS")
    private String address = null;

    @Column(name = "TAG")
    private String tag = null;

    @Column(name = "BOOK_TITLE")
    private String bookTitle = null;

    @Lob
    @Column(name = "BOOK_CONTENT")
    private String bookContent = null;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    @Column(name = "REGDATE", updatable = false)
    private Date regdate;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne
    @JoinColumn(name = "CATEGORY_CODE")
    private Category category;
}
