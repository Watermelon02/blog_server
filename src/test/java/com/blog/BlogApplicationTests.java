package com.blog;

import com.blog.controller.PassageController;
import com.blog.domain.Passage;
import com.blog.domain.response.Result;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class BlogApplicationTests {
    @Autowired
    PassageController passageController;

    @Test
    void test() {
        Result<List<Passage>> result = passageController.select(0);
        System.out.println(result.getTotal());
    }



}
