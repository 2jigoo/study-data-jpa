package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
public class Book extends Item {

    private String author;

    private String isbn;
}
