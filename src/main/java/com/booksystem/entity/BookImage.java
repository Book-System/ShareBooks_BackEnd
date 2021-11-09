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
@Table(name = "BOOKIMAGE")
@SequenceGenerator(name = "SEQ_BOOKIMAGE_NO", sequenceName = "SEQ_BOOKIMAGE_NO", initialValue = 1, allocationSize = 1)
public class BookImage {
    @Id
    @Column(name = "BOOKIMAGE_NO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BOOKIMAGE_NO")
    private Long bookimageNo;

    @Lob
    @Column(name = "IMAGE")
    private byte[] image;

    @Column(name = "IMAGESIZE")
    private Long imagesize;

    @Column(name = "IMAGETYPE")
    private String imagetype;

    @Column(name = "IMAGENAME")
    private String imagename;

    @Column(name = "PRIORITY")
    private int priority;

    @ManyToOne
    @JoinColumn(name = "BOOK_NO")
    private Book book;
}