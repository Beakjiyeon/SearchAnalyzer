package kr.co.tbase.searchad.entity;

import lombok.*;

import javax.persistence.Id;

import javax.persistence.*;

@ToString
@Getter
@Table(name = "keyword")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Keywords {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "keyword", length = 256, nullable = false)
    private String name;

    @Column(name = "url", length = 1024,nullable = false)
    private String url;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content",length = 1024)
    private String content;

    // 본문에서 검색어가 나타난 빈도
    @Column(name = "count", nullable = false)
    private int count;


    @Builder
    public Keywords(String name, String url, String title, String content, int count) {
        // int id,
        // this.id = id;
        this.name = name;
        this.url = url;
        this.title = title;
        this.content = content;
        this.count = count;
    }
}
