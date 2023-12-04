package ua.com.hastleuniversity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.hastleuniversity.domain.article.Article;
import ua.com.hastleuniversity.form.ArticleIdTitleForm;
import ua.com.hastleuniversity.repository.ArticleRepo;
import ua.com.hastleuniversity.repository.AuthorRepo;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepo articleRepo;

    private final AuthorRepo authorRepo;

    @Autowired
    public ArticleService(ArticleRepo articleRepo, AuthorRepo authorRepo) {
        this.articleRepo = articleRepo;
        this.authorRepo = authorRepo;
    }

    public Article saveArticle(Article article){
        return articleRepo.save(article);
    }

    public List<Article> getAllArticles(){
        return articleRepo.findAll();
    }

    public List<ArticleIdTitleForm> getArticleIdTitleFormByTerm(String term){
        return articleRepo.findIdTitleByRequestTermArticle(term);
    }


}
