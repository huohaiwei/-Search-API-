package com.yellow.usermanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;



@SpringBootTest
class UsermanagerApplicationTests {

    @Test
    void testDigest()  {
        String newPass= DigestUtils.md5DigestAsHex("abcd".getBytes(StandardCharsets.UTF_8));
        System.out.println(newPass);

    }

    @Test
    void contextLoads() {
    }

}
