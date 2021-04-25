package kr.co.tbase.searchad.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member")
public class Members {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(length = 20, nullable = false)
    private String userId;

    @Column(length = 100, nullable = false)
    private String pwd;

    @Column(length = 100, nullable = false)
    private String userName;

    @Column(length = 20, nullable = false)
    private String userType;

    @Builder
    public Members(int id, String userId, String pwd, String userName, String userType) {
        this.id = id;
        this.userId = userId;
        this.pwd = pwd;
        this.userName = userName;
        this.userType = userType;
    }

}