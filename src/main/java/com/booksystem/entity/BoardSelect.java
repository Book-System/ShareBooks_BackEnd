package com.booksystem.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.Immutable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Immutable
@Table(name = "BOOK_SELECT1")
public class BoardSelect {
    @Id
    @Column(name = "BOARD_NO")
    private Long boardNo;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "INQUIRY")
    private String inquiry;

    @Column(name = "RESERVATION_NO")
    private String reservationNo;

    @Column(name = "REGDATE")
    private Date regdate;

}
