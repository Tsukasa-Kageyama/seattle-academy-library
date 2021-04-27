package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class BookDetailsInfo {

    private int bookId;

    private String title;

    //4,項目の追加
    private String description;

    private String author;

    private String publisher;

    //4,
    private String publishDate;

    private String thumbnailUrl;

    private String thumbnailName;

    //4,項目の追加
    private String isbn;


    public BookDetailsInfo() {

    }

    public BookDetailsInfo(int bookId, String title, String author, String publisher, String publishDate,
            String thumbnailUrl, String thumbnailName, String description, String isbn) {
        this.bookId = bookId;
        this.title = title;
        //4,項目の追加
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        //4,
        this.publishDate = publishDate;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailName = thumbnailName;
        //4,項目の追加
        this.isbn = isbn;
    }

}