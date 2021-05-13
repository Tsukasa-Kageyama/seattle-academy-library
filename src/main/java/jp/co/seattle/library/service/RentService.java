package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class RentService {
    final static Logger logger = LoggerFactory.getLogger(RentService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * rentテーブルに追加する
     * @param bookId　書籍ID
     */
    public void rentBook(int bookId) {
        //4,description,isbn
        String sql = "INSERT INTO rent (book_id) VALUES (" + bookId + ")";

        jdbcTemplate.update(sql);
    }

    /**
     * rentテーブルに取得する
     * @param bookId　書籍ID
     * @return
     */
    public int rentCheck(int bookId) {
        String sql = "SELECT COUNT(BOOK_ID) FROM rent WHERE BOOK_ID =" + bookId;

        //質問
        int getRentId = jdbcTemplate.queryForObject(sql, Integer.class);

        return getRentId;
    }


    /**
     * rentテーブルのレコードを削除する
     * @param bookId　書籍ID
     */
    public void returnBook(int bookId) {
        String sql = "DELETE FROM rent WHERE BOOK_ID =" + bookId;

        jdbcTemplate.update(sql);

    }

}
