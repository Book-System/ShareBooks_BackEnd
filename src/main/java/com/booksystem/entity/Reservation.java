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
import javax.persistence.FetchType;

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
    @Column(name = "NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVATION_NO")
    private long no = 0L;

    @Column(name = "PHONE")
    private long phone = 0L;

    @Column(name = "RESERVATION_START_DATE")
    private String reservation_start_date = null;

    @Column(name = "RESERVATION_END_DATE")
    private String reservation_end_date = null;

    @Column(name = "RESERVATION_START_TIME")
    private String reservation_start_time = null;

    @Column(name = "RESERVATION_END_TIME")
    private String reservation_end_time = null;

    @Column(name = "REQUEST")
    private boolean request = false;

    @Column(name = "REQUEST_MESSAGE")
    private String request_message = null;

    @Column(name = "REJECT_MESSAGE")
    private String reject_message = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOOK_NO")
    private Book book;

}