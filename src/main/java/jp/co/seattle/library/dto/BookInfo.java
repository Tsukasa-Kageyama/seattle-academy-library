package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍基本情報格納DTO
 */
@Configuration
@Data
public class BookInfo {

    private int bookId;

    private String title;

    //項目の追加
    private String description;

    private String author;

    private String publisher;

    private String publishDate;

    private String thumbnail;

    //項目の追加
    private String isbn;


    public BookInfo() {

    }

    // コンストラクタ
    public BookInfo(int bookId, String title, String author, String publisher, String publishDate, String thumbnail,
            String description, String isbn) {
        this.bookId = bookId;
        this.title = title;
        //項目の追加
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.thumbnail = thumbnail;
        //項目の追加
        this.isbn = isbn;

    }

}