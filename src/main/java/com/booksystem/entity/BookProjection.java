package com.booksystem.entity;

import java.util.Date;

public interface BookProjection {
    Long getBook_No();

    String getTitle();

    Long getPrice();

    String getAddress();

    String getTag();

    String getBook_Title();

    Date getRegdate();
}