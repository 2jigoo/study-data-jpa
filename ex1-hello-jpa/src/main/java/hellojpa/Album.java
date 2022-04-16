package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
//@DiscriminatorValue("A") // name default entity name
@Getter @Setter
public class Album extends Item {

    private String artist;
}
