package study.datajpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    public Member(String username) {
        this.username = username;
    }

    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

}