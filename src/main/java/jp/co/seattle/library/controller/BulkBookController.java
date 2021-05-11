package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkBookController {
    final static Logger logger = LoggerFactory.getLogger(BulkBookController.class);

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/bulkBook", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String bulk(Model model) {
        return "bulkBook";
    }

    /**
     * @param locale
     * @param file アップロードされたcsvファイル
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/fileupload", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String bulkBook(Locale locale,
            @RequestParam("inputCsvFile") MultipartFile uploadFile,

            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        List<String[]> booklist = new ArrayList<String[]>();
        //エラー文はwhile文の外、エラー用のリスト
        List<String> errorList = new ArrayList<String>();
        int a = 0;

        String line = null;
        try {
            InputStream stream = uploadFile.getInputStream();
            Reader reader = new InputStreamReader(stream);
            BufferedReader buf = new BufferedReader(reader);
            while ((line = buf.readLine()) != null) {

                //カンマで区切り、それぞれの配列に格納
                //String[] strArray = (line.split(","));　←省略形
                String[] data = new String[6];
                data = line.split(",");
                booklist.add(data);
                a++;

                //必須項目はあるか
                //0~3までの要素チェック
                //StringUtils.isNullOrEmpty 空文字、nullどちらも
                if (StringUtils.isNullOrEmpty(data[0]) || StringUtils.isNullOrEmpty(data[1])
                        || StringUtils.isNullOrEmpty(data[2]) || StringUtils.isNullOrEmpty(data[3])) {
                    //エラーメッセージをlistに格納
                    errorList.add(a + "行目の必須項目がありません");
                }

                //文字列、形式は合っているか(バリデーションチェック)
                //出版日YYYYMMDD
                try {
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    df.setLenient(false);
                    df.parse(data[3]);
                } catch (ParseException p) {
                    //エラーメッセージをlistに格納
                    errorList.add(a + "行目の出版日は半角数字のYYYYMMDD形式で入力してください");

                }

                //Isbn (10,13)
                boolean isValidIsbn = data[4].matches("([0-9]{10}|[0-9]{13})?$");
                if (!isValidIsbn) {
                    //エラーメッセージをlistに格納
                    errorList.add(a + "行目のISBNの桁数または半角英数が正しくありません");

                }
            }


            //list内にエラーメッセージかあるかどうか
            if (!(errorList.size() == 0)) {
                model.addAttribute("errorMessage", errorList);
                return "bulkBook";
            }

            // パラメータで受け取った書籍情報をDtoに格納する。
            for (int i = 0; i < booklist.size(); i++) {
                BookDetailsInfo bookInfo = new BookDetailsInfo();
                bookInfo.setTitle(booklist.get(i)[0]);
                bookInfo.setAuthor(booklist.get(i)[1]);
                bookInfo.setPublisher(booklist.get(i)[2]);
                bookInfo.setPublishDate(booklist.get(i)[3]);
                bookInfo.setIsbn(booklist.get(i)[4]);
                bookInfo.setDescription(booklist.get(i)[5]);

                // 書籍情報を新規登録する
                booksService.registBook(bookInfo);
            }

            //登録した書籍の詳細情報を表示するように実装
            model.addAttribute("resultMessage", "登録完了");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //  詳細画面に遷移する
        return "bulkBook";
    }

}
