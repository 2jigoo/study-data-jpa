package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    // 2-1. 메소드명
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // 2-2. @NamedQuery
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 2-3. @Query
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    // 2-4. @Query 값 조회
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    // 2-4. DTO로 받기
    @Query("select new study.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDTO> findMemberDTO();

    // 2-5. 파라미터 바인딩
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    // 2-6. 반환 타입
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptByUsername(String username);

    // 2-7. page
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findPageByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    // 2-8. bulk update
    // 없으면 InvalidDataAccessApiUsageException
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkGetAYearOlder(@Param("age") int age);

    // 2-9. Fetch Join
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    // 2-9. Entity Graph (== Fetch Join)
    @Override
//    @EntityGraph(attributePaths = { "team" })
    @EntityGraph("Member.all")
    List<Member> findAll();

    @EntityGraph(attributePaths = { "team" })
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = { "team" })
    List<Member> findMemberEntityGraphByUsername(@Param("username") String username);

    // 2-10. JPA Hint & Lock

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    // Lock
    // select ... for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
