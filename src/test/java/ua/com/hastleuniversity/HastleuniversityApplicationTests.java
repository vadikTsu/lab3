package ua.com.hastleuniversity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.hastleuniversity.repository.ArticleRepo;
import ua.com.hastleuniversity.repository.AuthorRepo;
import ua.com.hastleuniversity.services.ArticleService;
import ua.com.hastleuniversity.services.FileParserService;

import javax.imageio.IIOException;
import java.io.IOException;

@SpringBootTest
class HastleuniversityApplicationTests {


    @Autowired
    private FileParserService parserService;
    @Autowired
    ArticleService articleService;


    @Test
    void contextLoads() throws IOException {
        parserService.serializeArticlesToFile(articleService.getAllArticles());
    }

}
