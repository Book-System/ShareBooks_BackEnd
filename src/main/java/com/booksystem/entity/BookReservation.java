package com.booksystem.entity;

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
@Table(name = "BOOK_RESERVATION")
public class BookReservation {
    @Id
    @Column(name = "RESERVATION_NO")
    private Long reservationNo;

    @Column(name = "RESERVATION_END_DATE")
    private String reservationEndDate;

    @Column(name = "BOOK_TITLE")
    private String bookTitle;

    @Column(name = "MEMBER_ID")
    private String memberId;

}
