package com.booksystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Id;

import javax.persistence.SequenceGenerator;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "Category")
@SequenceGenerator(name = "SEQ_CATEGORY_NO", sequenceName = "SEQ_CATEGORY_NO", initialValue = 1, allocationSize = 1)
public class Category {

    @Id
    @Column(name = "CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CATEGORY_NO")
    private Long no = 0L;

    @Column(name = "NAME")
    private String name = null;
}
