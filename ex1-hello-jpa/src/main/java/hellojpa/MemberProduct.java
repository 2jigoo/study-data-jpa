package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;

// 의미 있는 이름을 갖자면 ORDERS가 되겠지
@Entity
public class MemberProduct {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    private int count;
    private int price;
    private LocalDateTime orderDateTime;

}
