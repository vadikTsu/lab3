package ua.com.hastleuniversity.domain.article;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String imageUrl;

    private String text;

    @Past
    @Column(name = "publish_date")
    private LocalDate publishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Author author;

    @Override
    public String toString() {
        return "{" +
                "id=" + id + "\n"+
                ", title='" + title + "\n"+
                ", imageUrl='" + imageUrl + "\n"+
                ", text='" + text + "\n"+
                ", publishDate=" + publishDate +"\n"+
                ", author=" + author +"\n"+
                '}';
    }
}
