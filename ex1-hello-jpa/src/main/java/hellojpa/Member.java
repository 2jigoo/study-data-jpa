package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;


    // 편의 메서드를 양쪽에 모두 두면 문제를 일으킬 수도 있다. 둘 중 하나 정해서 사용하기.
    /*
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }*/

}
