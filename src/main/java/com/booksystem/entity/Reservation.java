package com.booksystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "RESERVATION")
@SequenceGenerator(name = "SEQ_RESERVATION_NO", sequenceName = "SEQ_RESERVATION_NO", initialValue = 1, allocationSize = 1)
public class Reservation {

    @Id
    @Column(name = "RESERVATION_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVATION_NO")
    private Long reservationNo = 0L;

    @Column(name = "PHONE")
    private String phone = null;

    @Column(name = "RESERVATION_START_DATE")
    private String reservationStartDate = null;

    @Column(name = "RESERVATION_END_DATE")
    private String reservationEndDate = null;

    @Column(name = "RESERVATION_START_TIME")
    private String reservationStartTime = null;

    @Column(name = "RESERVATION_END_TIME")
    private String reservationEndTime = null;

    @Column(name = "REQUEST")
    private boolean request = false;

    @Column(name = "REQUEST_MESSAGE")
    private String requestMessage = null;

    @Column(name = "REJECT_MESSAGE")
    private String rejectMessage = null;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "BOOK_NO")
    private Book book;

}
