package study.datajpa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import study.datajpa.entity.Member;

@Getter @Setter @ToString
@NoArgsConstructor
public class MemberDTO {

    private Long id;
    private String username;
    private int age;
    private String teamName;

    public MemberDTO(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDTO(Long id, String username, int age, String teamName) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.teamName = teamName;
    }

    public static MemberDTO of(Member member) {
        return new MemberDTO(member.getId(), member.getUsername(), member.getAge(), member.getTeam().getName());
    }
}
