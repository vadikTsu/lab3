package ua.com.hastleuniversity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.hastleuniversity.form.ArticleIdTitleForm;
import ua.com.hastleuniversity.services.ArticleService;

import java.util.Collection;
import java.util.List;


@Controller
@RequestMapping()
public class HomeController {

    private final ArticleService articleService;

    @Autowired
    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }
    @GetMapping("about")
    public String about(){
        return "about";
    }
    @GetMapping("/search")
    public ResponseEntity<List<ArticleIdTitleForm>> searchAdviceForArticles(@RequestParam("term") String term){
        List<ArticleIdTitleForm> advices = articleService.getArticleIdTitleFormByTerm(term);
        return ResponseEntity.ok(advices);
    }

    @ModelAttribute
    public void loadArticles(Model model){
        model.addAttribute("articles", articleService.getAllArticles());
    }

}
