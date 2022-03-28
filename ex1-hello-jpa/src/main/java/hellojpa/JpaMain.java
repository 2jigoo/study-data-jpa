package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {
//            selectList(em);
//            insertAndCache(em);

            Member findMember1 = em.find(Member.class, 100L);
            Member findMember2 = em.find(Member.class, 100L);

            System.out.println("findMember == findMember2 = " + (findMember1 == findMember2));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();; // 내부적으로 DB 커넥션을 얻어온다.
        }

        emf.close(); // application 종료시
    }

    private static void insertAndCache(EntityManager em) {
        Member member = new Member();
        member.setId(100L);
        member.setName("Hello JPA");

        System.out.println("=== BEFORE ===");
        em.persist(member);
        System.out.println("=== AFTER ===");

        Member findMember = em.find(Member.class, 100L);

        System.out.println("findMember.id = " + findMember.getId());
        System.out.println("findMember.name = " + findMember.getName());
    }

    private static void selectList(EntityManager em) {
        Member findMember = em.find(Member.class, 1L);
        findMember.setName("이맘바");

        List<Member> members = em.createQuery("select m from Member as m", Member.class)
                .setFirstResult(5)
                .setMaxResults(8)
                .getResultList();

        for (Member member : members) {
            System.out.println("member.name: " + member.getName());
        }
    }
}
