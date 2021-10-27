package com.booksystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "BOARDIMAGE")
@SequenceGenerator(name = "SEQ_BOARDIMAGE_NO", sequenceName = "SEQ_BOARDIMAGE_NO", initialValue = 1, allocationSize = 1)
public class BoardImage {
    @Id
    @Column(name = "NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOARDIMAGE_NO")
    private Long no;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image = null;

    @Column(name = "IMAGENAME")
    private String imagename = null;

    @Column(name = "IMAGESIZE", nullable = false, columnDefinition = "long default 0")
    private Long imagesize = 0L;

    @Column(name = "IMAGETYPE")
    private String imagetype = null;

    @Column(name = "PRIORITY")
    private int priority = 0;

    @ManyToOne
    @JoinColumn(name = "BOARD_NO")
    private Board board;
}
