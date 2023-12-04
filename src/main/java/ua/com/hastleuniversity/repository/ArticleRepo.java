package ua.com.hastleuniversity.repository;

import org.springframework.stereotype.Repository;
import ua.com.hastleuniversity.domain.article.Article;
import ua.com.hastleuniversity.form.ArticleIdTitleForm;

import java.util.List;

@Repository
public class ArticleRepo extends AbstractRepo<Article>{

    public ArticleRepo(){
        super();
        setEntity(Article.class);
    }
    public List<ArticleIdTitleForm> findIdTitleByRequestTermArticle(String term) {
        return entityManager.createQuery("SELECT new ua.com.hastleuniversity.form.ArticleIdTitleForm(a.id, a.title) FROM Article a WHERE LOWER(a.title) LIKE :str",
        ArticleIdTitleForm.class).setParameter("str", "%" + term.toLowerCase() + "%").getResultList();
    }

}
