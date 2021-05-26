package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.RentInfo;
import jp.co.seattle.library.rowMapper.RentRowMapper;

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
     * 選択された書籍の貸出情報を検索
     * @param bookId　書籍ID
     * @return
     */
    public int rentCheck(int bookId) {
        //rentTBL内から選択された書籍のIDを元に、一番数の大きいrentID（貸出ID）を検索
        String rentMaxID = "SELECT MAX(RENT_ID) FROM rent WHERE BOOK_ID =" + bookId;
        Integer rentId = jdbcTemplate.queryForObject(rentMaxID, Integer.class);

        if (rentId == null) {//貸出IDがない場合（一度も貸出が行われていない）、DELETED_FLAG＝1（貸出可の状態）で返す。
            return 1;
        }

        //検索されたrentIDを元にDELETED_FLAG（論理削除）を検索
        String sql = "SELECT DELETED_FLAG FROM rent WHERE RENT_ID =" + rentId;
        int getRentId = jdbcTemplate.queryForObject(sql, Integer.class);

        return getRentId;
    }

    /**
     * rentテーブルのレコードを論理削除する
     * @param bookId　書籍ID
     */
    public void returnBook(int bookId) {
        String sql = "UPDATE rent SET DELETED_FLAG=1 , RETURN_DATE=CURRENT_TIMESTAMP  WHERE BOOK_ID=" + bookId;

        jdbcTemplate.update(sql);

    }

    public List<RentInfo> getRentList(int bookId) {

        List<RentInfo> getedRentList = jdbcTemplate.query(
                "SELECT RENT_DATE, RETURN_DATE FROM rent WHERE BOOK_ID = " + bookId + ";",
                new RentRowMapper());

        return getedRentList;

    }

}
