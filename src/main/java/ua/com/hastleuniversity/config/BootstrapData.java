package ua.com.hastleuniversity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ua.com.hastleuniversity.domain.article.Article;
import ua.com.hastleuniversity.domain.article.Author;
import ua.com.hastleuniversity.repository.ArticleRepo;
import ua.com.hastleuniversity.repository.AuthorRepo;
import ua.com.hastleuniversity.services.FileParserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Component
public class BootstrapData implements ApplicationRunner {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Author author1 = Author.builder().authorName("vadym").authorPosition("Student").articles(new HashSet<>()).authorEmail("vadym@gmail.com").build();
        Article article1 = Article.builder()
                .publishDate(LocalDate.now()).title("Great opportunities for student")
                .text("Not Yet Implemented").build();

        Article article2 = Article.builder()
                .publishDate(LocalDate.now()).title("Magic design Patterns")
                .text("Not Yet Implemented").build();
        Article article3 = Article.builder()
                .publishDate(LocalDate.now()).title("Designing software with AJAX")
                .text("Not Yet Implemented").build();
        Article article4 = Article.builder()
                .publishDate(LocalDate.now()).title("Learning Thymeleaf")
                .text("Not Yet Implemented").build();
        Article article5 = Article.builder()
                .publishDate(LocalDate.now()).title("Road Map for Java Spring")
                .text("Not Yet Implemented").build();

        author1.getArticles().add(article1);
        author1.getArticles().add(article2);
        author1.getArticles().add(article3);
        author1.getArticles().add(article4);
        author1.getArticles().add(article5);
        article1.setAuthor(author1);
        article2.setAuthor(author1);
        article3.setAuthor(author1);
        article4.setAuthor(author1);
        article5.setAuthor(author1);
        authorRepo.save(author1);
        articleRepo.save(article1);
        articleRepo.save(article2);
        articleRepo.save(article3);
        articleRepo.save(article4);
        articleRepo.save(article5);
    }
}
