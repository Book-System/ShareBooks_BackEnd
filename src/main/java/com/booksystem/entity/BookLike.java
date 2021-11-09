package com.booksystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "BOOKLIKE")
@SequenceGenerator(name = "SEQ_BOOKLIKE_NO", sequenceName = "SEQ_BOOKLIKE_NO", initialValue = 1, allocationSize = 1)
public class BookLike {

    @Id
    @Column(name = "BOOKLIKE_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOKLIKE_NO")
    private Long booklikeNo = 0L;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BOOK_NO")
    private Book book;
}
