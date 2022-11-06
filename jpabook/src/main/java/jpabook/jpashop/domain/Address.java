package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter @Setter(AccessLevel.PRIVATE)
public class Address {

    @Column(length = 50)
    private String city;

    @Column(length = 100)
    private String street;

    @Column(length = 5)
    private String zipcode;

    private String getFullAddress() {
        return "[" + getZipcode() + "]" + getCity() + " " + getStreet();
    }

    // 의미 있는 메소드 사용 가능. 검증 로직 등
//    public boolean isValid()

    // field에 직접 접근할 때는 프록시일 때 계산이 안 됨
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getStreet(), address.getStreet()) &&
                Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }

}
