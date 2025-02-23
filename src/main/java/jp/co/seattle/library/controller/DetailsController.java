package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

/**
 * 詳細表示コントローラー
 */
@Controller
public class DetailsController {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);

    @Autowired
    private BooksService bookdService;

    @Autowired
    private RentService rentService;

    /**
     * 詳細画面に遷移する
     * @param locale
     * @param bookId
     * @param model
     * @return
     */
    @Transactional
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public String detailsBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);

        int getRentId = rentService.rentCheck(bookId);
        if (getRentId == 1) { //rentテーブルのDELETED_FLAGが0の場合（貸出可）
            model.addAttribute("rentOk", "貸出し可");
        } else {
            model.addAttribute("rentNg", "貸出し中");
        }

        if (CollectionUtils.isEmpty(rentService.getRentList(bookId))) {
            model.addAttribute("errorMessage", "貸出履歴がありません");
            model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));

            return "details";
        }

        model.addAttribute("bookDetailsInfo", bookdService.getBookInfo(bookId));
        model.addAttribute("rentList", rentService.getRentList(bookId));

        return "details";
    }
}
