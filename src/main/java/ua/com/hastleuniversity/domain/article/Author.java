package ua.com.hastleuniversity.domain.article;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String authorName;
    private String authorPosition;
    private String authorEmail;

    @OneToMany(mappedBy = "author", cascade = { CascadeType.ALL })
    private Set<Article> articles = new HashSet<>();
}
