package ua.com.hastleuniversity.repository;

import org.springframework.stereotype.Repository;
import ua.com.hastleuniversity.domain.article.Author;

@Repository
public class AuthorRepo extends AbstractRepo<Author> {

    public AuthorRepo(){
        super();
        setEntity(Author.class);
    }
}
