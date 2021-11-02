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
@Table(name = "BOARDREPLY")
@SequenceGenerator(name = "SEQ_BOARDREPLY_NO", sequenceName = "SEQ_BOARDREPLY_NO", initialValue = 1, allocationSize = 1)
public class BoardReply {
    @Id
    @Column(name = "BOARDREPLY_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARDREPLY_NO")
    private Long boardreplyNo;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "BOARD_NO")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
