package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    // 주 테이블에 외래키 일대일 단/양방향
    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;


    // 편의 메서드를 양쪽에 모두 두면 문제를 일으킬 수도 있다. 둘 중 하나 정해서 사용하기.
    /*
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }*/


    // 다대다 단방향
    /*@ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT") // 연결 테이블
    private List<Product> products = new ArrayList<>();*/

    // 다대다 대신에 일대다, 다대일로 풀기
    // 연결 테이블을 엔티티로 승격
    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();


}
