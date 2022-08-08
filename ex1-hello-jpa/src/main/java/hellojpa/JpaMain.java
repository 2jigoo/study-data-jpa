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

//            printMemberAndTeam(findMember);
//            printRefrence(em, member);

            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
            em.persist(child1);
            em.persist(child2);


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();; // 내부적으로 DB 커넥션을 얻어온다.
        }

        emf.close(); // application 종료시
    }

    private static void printRefrence(EntityManager em) {
        Member member = new Member();
        member.setUsername("hello");

        em.persist(member);

        em.flush();
        em.clear();

        //            Member findMember = em.find(Member.class, 1L);
        Member findMember = em.getReference(Member.class, member.getId());
        System.out.println("findMember.class = " + findMember.getClass()); // Member$HibernateProxy$...
        System.out.println("findMember.id = " + findMember.getId()); // 이미 값이 있기 때문에 쿼리 안 나감
        System.out.println("findMember.username = " + findMember.getUsername()); // 이때 SELECT 쿼리 나감
    }

    private static void printMemberAndTeam(EntityManager em) {
        Member member = new Member();
        member.setUsername("hello");

        em.persist(member);

        em.flush();
        em.clear();

        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }


}
