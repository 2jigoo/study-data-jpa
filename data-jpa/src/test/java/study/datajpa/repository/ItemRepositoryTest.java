package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void save() {
        // persist 되지 않은 새 엔티티 판별 기준
        // - 식별자가 reference type일 때, null인지?
        // - 식별자가 primitive type일 때, 기본값인지?
        Item item = new Item("A");
        itemRepository.save(item);

        // PK에 값이 있기 때문에 persist가 아닌 merge 호출
        // DB에서 조회해서 값이 없으면 새로운 엔티티로 인지
        // 비효율적이기 때문에 엔티티에서 Persistable을 구현하여 새 엔티티 판별 로직을 직접 구현

    }

}