package com.booksystem.entity;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Lob;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "Book")
@SequenceGenerator(name = "SEQ_BOOK_NO", sequenceName = "SEQ_BOOK_NO", initialValue = 1, allocationSize = 1)
public class Book {

    @Id
    @Column(name = "NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOK_NO")
    private Long no = 0L;

    @Column(name = "TITLE")
    private String title = null;

    @Lob
    @Column(name = "CONTENT")
    private String content = null;

    @Column(name = "PRICE")
    private long price = 0L;

    @Column(name = "ADDRESS")
    private String address = null;

    @Column(name = "TAG")
    private String tag = null;

    @Column(name = "BOOK_TITLE")
    private String book_title = null;

    @Lob
    @Column(name = "BOOK_CONTENT")
    private String book_content = null;

    @CreationTimestamp
    @Column(name = "REGDATE")
    private Date regdate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CATEGORY_CODE")
    private Category category;
}
