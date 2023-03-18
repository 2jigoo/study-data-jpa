package study.datajpa.repository.projection;

public class UsernameDTO {

    private final String username;

    // 생성자의 파라미터명을 사용한다
    public UsernameDTO(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
