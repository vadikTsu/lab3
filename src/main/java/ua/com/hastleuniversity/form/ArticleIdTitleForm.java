package ua.com.hastleuniversity.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleIdTitleForm {

    private long id;

    private String title;

    public ArticleIdTitleForm(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
