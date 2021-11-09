package com.booksystem.entity;

public interface BookImageProjection {
    Long getBookimage_No();

    byte[] getImage();

    Long getImagesize();

    String getImagetype();

    String getImagename();

    int getPriority();
}