package kr.co.tbase.searchad.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.Id;

import javax.persistence.*;

@Getter
@Table(name = "keyword")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Keywords {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "keyword", length = 256, nullable = false)
    private String name;

    @Column(name = "count", nullable = false)
    private int age;
}
