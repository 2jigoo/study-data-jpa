package study.datajpa.repository.projection;

import org.springframework.beans.factory.annotation.Value;

public interface SimpleData {

    // closed-projection
    String getUsername();

    // opened-projection
    @Value("#{target.username + ' ' + target.age}")
    String getInfo();

}
