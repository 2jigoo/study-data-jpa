package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");

            Address address = new Address("city1", "street", "01234");
            member.setHomeAddress(address);

            Set<String> favoriteFoods = member.getFavoriteFoods();
            favoriteFoods.add("치킨");
            favoriteFoods.add("족발");
            favoriteFoods.add("피자");

            member.getAddressHistory().add(new AddressEntity("old1", "street", "12345"));
            member.getAddressHistory().add(new AddressEntity("old2", "street", "12345"));

            em.persist(member);


            em.flush();
            em.clear();

            // Member만 가져옴
            Member findMember = em.find(Member.class, member.getId());

            List<AddressEntity> addressHistory = findMember.getAddressHistory();
            for (AddressEntity a : addressHistory) {
                System.out.println("address: " + a.getAddress());
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();; // 내부적으로 DB 커넥션을 얻어온다.
        }

        emf.close(); // application 종료시
    }


}
