package kr.co.tbase.searchad.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Table(name = "host")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Hosts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    //@Id
    @Column(name = "keyword", length = 256, nullable = false)
    private String name;

    //@Id
    @Column(name = "domain", length = 1024)
    private String domain;

    // 누적수
    @Column(name = "count", nullable = false)
    private int count;

    @Builder
    public Hosts(String name, String domain, int count) {
        this.name = name;
        this.domain = domain;
        this.count= count;
    }
}
