package jp.co.seattle.library.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.jdbc.StringUtils;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //details.jspから探す目標
public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

    @Autowired
    //自動的にインスタンス化　BooksService　newしてbooksService
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    //7,details.jsp から飛ぶ
    // @RequestMapping　jspとjavaを繋ぐアドレス
    @Transactional
    @RequestMapping(value = "/editBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得                                      //GET→POST　details.jspのmethod="post"
    public String editBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            //7.RequestParam　jspの変数を受け取る
            Model model) {//
        model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
        return "editBook";
    }



    /**
     * 書籍情報を更新する
     *パラムは引数
     * @param locale ロケール情報
     * @param locale ID
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    //7
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST)
    public String updateBook(Locale locale,
            //7.bookIdで既にある情報を表示する
            //リクエストパラメータとはクライアント側のフォームから送られてきたデータ
            @RequestParam("bookId") Integer bookId,
            //7.更新するための新たな箱を追加する
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("publishDate") String publishDate,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("isbn") String isbn,

            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        //7,bookId
        bookInfo.setBookId(bookId);
        bookInfo.setTitle(title);
        bookInfo.setDescription(description);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);

        // クライアントのファイルシステムにある元のファイル名を設定する
        String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "editBook";
            }
        }

        //必須項目
        if (StringUtils.isNullOrEmpty(title) || StringUtils.isNullOrEmpty(author)
                || StringUtils.isNullOrEmpty(publisher)
                || StringUtils.isNullOrEmpty(publishDate)) {
            model.addAttribute("errorMessage", "必須項目を入力してください");
            return "editBook";
        }

        //出版日YYYYMMDD
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            df.setLenient(false);
            df.parse(publishDate);
        } catch (ParseException p) {
            model.addAttribute("errorMessage", "ISBNの桁数または半角英数が正しくありません<br>出版日は半角数字のYYYYMMDD形式で入力してください");
            return "editBook";
        }

        //　バリデーションチェック
        //Isbn (10,13)
        boolean isValidIsbn = isbn.matches("([0-9]{10}|[0-9]{13})?$");

        //if文
        if (!isValidIsbn) {
            //フロントにエラーメッセージを返す
            model.addAttribute("errorMessage", "ISBNの桁数または半角英数が正しくありません<br>出版日は半角数字のYYYYMMDD形式で入力してください");
            //returnで返す
            return "editBook";
        }

        // 書籍情報を新規登録する
        booksService.editBook(bookInfo);

        // TODO 登録した書籍の詳細情報を表示するように実装
        model.addAttribute("resultMessage", "更新完了");
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //  詳細画面に遷移する
        return "details";
    }

}
