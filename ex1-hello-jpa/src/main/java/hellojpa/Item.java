package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // name default DTYPE
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private int price;

}
