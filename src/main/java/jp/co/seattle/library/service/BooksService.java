package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        //　取得したい情報 [id、書籍名、出版社、著者、出版日、サムネイル]
        List<BookInfo> getedBookList = jdbcTemplate.query(
                //4,description,isbn
                "select id,title,description,author,publisher,publish_date,thumbnail_url,isbn from books ORDER BY title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * bookIdのみを取得するメソッド
     */

    public int getBookId() {

        //IDの最大値を取得する
        String sql = "SELECT MAX(ID) FROM books";

        int getBookId = jdbcTemplate.queryForObject(sql, Integer.class);

        return getBookId;
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {
        //4,description,isbn
        String sql = "INSERT INTO books (title,description, author,publisher,publish_date,thumbnail_name,thumbnail_url,isbn,reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getDescription() + "','" + bookInfo.getAuthor() + "','"
                + bookInfo.getPublisher() + "','" + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','" + bookInfo.getIsbn() + "',"
                + "sysdate(),"
                + "sysdate())";

        jdbcTemplate.update(sql);
    }

    //更新
    public void editBook(BookDetailsInfo bookInfo) {
        String sql = "UPDATE books set title = '" + bookInfo.getTitle() + "',"
                + "description = '" + bookInfo.getDescription() + "',"
                + "author = '" + bookInfo.getAuthor() + "',"
                + "publisher = '" + bookInfo.getPublisher() + "',"
                + "publish_date = '" + bookInfo.getPublishDate() + "',"
                + "thumbnail_name = '" + bookInfo.getThumbnailName() + "',"
                + "thumbnail_url = '" + bookInfo.getThumbnailUrl() + "',"
                + "isbn = '" + bookInfo.getIsbn() + "',"
                + "upd_date = sysdate() where Id =" + bookInfo.getBookId();

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍を削除する
     *
     * @param bookInfo 書籍情報
     * @param bookId 書籍ID
     */

    public void deletingBook(int bookId) {
        String sql = "DELETE FROM books WHERE id =" + bookId;
        jdbcTemplate.update(sql);
    }

    /**
     * 入力された書籍名からIDを取得
     * @param searchWord 入力された検索ワード
     * @return
     */
    public List<BookInfo> getSearchList(String searchWord) {

        List<BookInfo> getedBookList = jdbcTemplate.query(
                "SELECT * FROM books WHERE TITLE LIKE '%" + searchWord + "%'ORDER BY title asc;",
                new BookInfoRowMapper());

        return getedBookList;
    }

}
