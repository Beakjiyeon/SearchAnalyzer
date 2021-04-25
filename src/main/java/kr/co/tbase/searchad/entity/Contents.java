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

    @Column(name = "relatedwords", length = 2048)
    private String relatedwords;

    @Builder
    public Contents(String keyword, String relatedwords) {
        this.keyword = keyword;
        this.relatedwords = relatedwords;
    }
}
