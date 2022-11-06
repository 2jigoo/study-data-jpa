package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    @Embedded
    private Period workPeriod;

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD"
            , joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    /*@ElementCollection
    @CollectionTable(name = "ADDRESS"
            , joinColumns = @JoinColumn(name = "MEMBER_ID")
    )
    private List<Address> addressHistory = new ArrayList<>();*/

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;


}
