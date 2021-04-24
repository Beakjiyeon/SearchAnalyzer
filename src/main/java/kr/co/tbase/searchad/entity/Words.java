package kr.co.tbase.searchad.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Table(name = "word")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Words {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "keyword", length = 256, nullable = false)
    private String name;

    @Column(name = "word", length = 256, nullable = false)
    private String word;

    // 본문 속 단어 별 누적 수
    @Column(name = "count", length = 256, nullable = false)
    private int count;

    @Column(name = "searchCount", length = 256, nullable = false)
    private int searchCount;

    @Column(name = "flag", length = 50, nullable = false)
    private String flag;

    @Builder
    public Words(String name, String word, int count, int searchCount, String flag) {
        this.name = name;
        this.word = word;
        this.count= count;
        this.searchCount = searchCount;
        this.flag= flag;
    }
}
