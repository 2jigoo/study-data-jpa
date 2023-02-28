package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 도메인 클래스 컨버터 (권장하지 않음)
     * 트랜잭션이 없는 상태에서 조회했기 때문에 단순 조회용으로 사용해야 함.
     */
    @GetMapping("/members-converter/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * {@code spring.data.web.pageable.one-indexed-parameters}를 {@code true}로 설정해도
     * index가 0부터 시작하는 문제가 있음.
     * Custom PageRequest, Page를 만들어 변환하는 방법 권장.
     * <pre>
     * {@code
     * PageRequest request = PageRequest.of(1, 10);
     * Page<Member> page = memberRepository.findAll(request).map(MemberDTO::of);
     * MyPage<Member> myPage = MyPage.of(page);
     * return myPage;
     * }
     * </pre>
     */
    @GetMapping("/members")
    public Page<MemberDTO> list(
            @PageableDefault(size = 20, sort = "username", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 직접 엔티티를 반환해선 안 된다.
        return memberRepository.findAll(pageable)
                .map(MemberDTO::of);
    }


    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }

}
