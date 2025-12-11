package com.example;

import com.example.domain.User;
import com.example.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class SpringSecurity2ApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        System.out.println(1);
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    void testEncode(){
        String encode = passwordEncoder.encode("1234");
        System.out.println( encode);
    }

}
