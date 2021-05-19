package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysql.jdbc.StringUtils;

import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class SearchController {
    final static Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private BooksService booksService;

    /**
     * 検索
     * @param locale
     * @param searchWord　入力された検索ワード
     * @param model
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String serchTitle(Locale locale,
            @RequestParam("searchWord") String searchWord,
            Model model) {

        if (StringUtils.isNullOrEmpty(searchWord)) {
            model.addAttribute("errorMessage", "入力してください");
            return "home";
        }

        if (CollectionUtils.isEmpty(booksService.getSearchList(searchWord))) {
            model.addAttribute("errorMessage", "該当する書籍名がありません");
            return "home";
        }

        model.addAttribute("bookList", booksService.getSearchList(searchWord));
        return "home";
    }
}
