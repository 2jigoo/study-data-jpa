package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.projection.NestedClosedProjections;
import study.datajpa.repository.projection.UsernameDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Optional<Member> optMember = memberRepository.findById(savedMember.getId());
        Member findMember = optMember.get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);

        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }
    
    @Test
    public void testFindUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s: " + s);
        }
    }

    @Test
    public void testFindMemberDTO() {
        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10, team);
        memberRepository.save(m1);

        List<MemberDTO> list = memberRepository.findMemberDTO();
        for (MemberDTO dto : list) {
            System.out.println("dto: " + dto);
        }
    }

    @Test
    public void testFindByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member: " + member);
        }
    }

    @Test
    public void testReturnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        // 조회 결과가 없을 때
        List<Member> list = memberRepository.findListByUsername("AAA"); // Collection의 경우 null이 아니라 empty
        /*
        Member member = memberRepository.findMemberByUsername("AAA"); // null
        Optional<Member> optMember = memberRepository.findOptByUsername("AAA"); // Optional.empty
        */
    }

    @Test
    public void testPaging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when
        Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
//        Slice<Member> slice = memberRepository.findSlice(age, pageRequest);

        // 엔티티를 외부에 노출 시키면 안 된다. 컨트롤러에서 반환할 땐 DTO로 변환하기.
        Page<MemberDTO> dtoPage = page.map(MemberDTO::of);

        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); // Slice X

        for (Member member : content) {
            System.out.println("member: " + member);
        }
        System.out.println("totalElements: " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(totalElements).isEqualTo(5); // Slice X
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2); // Slice X
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void testBulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // jpql 실행 전에 flush 하고 jpql 쿼리를 실행한다.
        int affectedRows = memberRepository.bulkGetAYearOlder(20);

        // 영속성 컨텍스트에 bulk update의 결과가 반영되지 않기 때문에, 영속성 컨텍스트를 초기화 해줘야한다.
        // @Modifying(clearAutomatically = true)로 대체할 수 있다.
//        em.flush();
//        em.clear();

        Member member5 = memberRepository.findMemberByUsername("member5");
        System.out.println(member5);

        assertThat(affectedRows).isEqualTo(3);
        assertThat(member5.getAge()).isEqualTo(41);
    }

    @Test
    public void findMemberLazy() {
        // given

        // member1 -> teamA
        // member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamA);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();


        // when N + 1 (쿼리를 한 번 날렸지만 결과의 개수 N만큼 쿼리 전송이 더 발생한다.)
        /*
        List<Member> members = memberRepository.findAll();                              // select * from member

        // Team의 데이터를 사용하는 시점에 조회 쿼리 나가게 된다.
        for (Member member : members) {
            System.out.println("member: " + member.getUsername());
            System.out.println("member.team.class: " + member.getTeam().getClass());    // entity.Team$HibernateProxy
            System.out.println("member.team: " + member.getTeam().getName());           // select * from team where team_id = ? 발생
        }
        */

        // when
        List<Member> members = memberRepository.findMemberFetchJoin();                  // select * from member left outer join team

        for (Member member : members) {
            System.out.println("member: " + member.getUsername());
            System.out.println("member.team.class: " + member.getTeam().getClass());    // entity.Team (실제 엔티티 객체)
            System.out.println("member.team: " + member.getTeam().getName());
        }
    }


    @Test
    public void testQueryHint() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");

        // readOnly로 설정했기 때문에 스냅샷이 없고 변경감지 하지 않는다. update 발생 X
        findMember.setUsername("member2");
        em.flush();

    }


    @Test
    public void testLock() {
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // select ... for update
        List<Member> findMembers = memberRepository.findLockByUsername("member1");

    }

    @Test
    public void testCallCustomRepository() {
        List<Member> findMember = memberRepository.findMemberCustom();
    }

    @Test
    public void testSpecBasic() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        Specification<Member> spec = MemberSpec.username("m1").and(MemberSpec.teamName("teamA"));
        List<Member> result = memberRepository.findAll(spec);

        // then
        Assertions.assertThat(result.size()).isEqualTo(1);
    }


    @Test
    public void queryByExample() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
        // Probe: 필드에 데이터가 있는 실제 도메인 객체(엔티티 자체가 검색 조건이 된다)
        Member member = new Member("m1");
        Team team = new Team("teamA");
        member.setTeam(team); // INNER JOIN

        // ExampleMatcher: 특정 필드를 일치시키는 상세 정보 제공, 재사용 가능
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("age");

        // Example: Probe와 ExampleMatcher로 구성. 쿼리 생성하는 데 사용
        Example<Member> example = Example.of(member, matcher);

        List<Member> result = memberRepository.findAll(example);
        
        // 실무 사용 기준 - JOIN이 해결이 되는지

        // then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo("m1");

        // 실무에서 사용하기엔 매칭 조건이 너무 단순하고 LEFT JOIN이 안 됨
    }

    @Test
    public void testProjection() {
        // given
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        // when
//         AbstractJpaQuery$TupleConverter$TupleBackedMap
//        List<SimpleData> result = memberRepository.findUsingProjectionsByUsername("m1", SimpleData.class);

//        List<UsernameDTO> result = memberRepository.findUsingProjectionsByUsername("m1", UsernameDTO.class);
//        result.forEach(System.out::println);

        List<NestedClosedProjections> result = memberRepository.findUsingProjectionsByUsername("m1", NestedClosedProjections.class);
        result.forEach(res -> {
            String username = res.getUsername();
            String teamName = res.getTeam().getName();
            System.out.println("username: " + username + ", teamName: " + teamName);
        });
    }

}