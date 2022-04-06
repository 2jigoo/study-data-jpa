package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    // 잘못된 설계. 예제 설명을 위해 넣음. 비지니스를 끊어주는게 더 깔끔.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
