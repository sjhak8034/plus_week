package com.example.demo.entity;

import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ItemTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @Rollback(true)
    void statusNullTest() throws NoSuchFieldException, IllegalAccessException, InterruptedException, ChangeSetPersister.NotFoundException {
        //given
        String name = "name";
        String description = "description";
        User manager = new User("user","email","user","asdf" );
        User owner = new User("user","email","user","asdf" );
        Item item = new Item("item","description",manager, owner);
        Field itemStatusField = Item.class.getDeclaredField("status");
        itemStatusField.setAccessible(true);
        itemStatusField.set(item,null);


        //when
        userRepository.save(manager);
        userRepository.save(owner);
        //then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> itemRepository.save(item));

    }
}


