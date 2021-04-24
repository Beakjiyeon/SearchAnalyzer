package kr.co.tbase.searchad.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
@Table(name = "content")
@Entity
public class Contents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "keyword", length = 256, nullable = false)
    private String keyword;

    @Column(name = "relatedWords", length = 2048)
    private String relatedWords;

    @Builder
    public Contents(String keyword, String relatedWords) {
        this.keyword = keyword;
        this.relatedWords = relatedWords;
    }
}
