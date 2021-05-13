package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

/**
 * 貸出機能
 */
@Controller /** APIの入り口 */
public class ReturnController {
    final static Logger logger = LoggerFactory.getLogger(ReturnController.class);

    @Autowired
    private RentService rentService;

    @Autowired
    private BooksService booksService;

    /**
     * @param locale
     * @param bookId 詳細画面上の本のID
     * @param model
     * @return
     */
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST)
    public String returnBook(Locale locale,

            //詳細画面上の本のID取得
            @RequestParam("bookId") Integer bookId,

            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        //取得した本のIDをrentテーブルに投げる
        rentService.returnBook(bookId);
        model.addAttribute("rentOk", "貸出し可");
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        return "details"; //jspファイル名
    }

}

