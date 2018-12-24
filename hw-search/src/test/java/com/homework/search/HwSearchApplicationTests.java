package com.homework.search;

import com.homework.search.model.PostDocument;
import com.homework.search.repository.PostRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HwSearchApplicationTests {

    @Autowired
    PostRepository postRepository;

    @Test
    public void contextLoads() {

        Iterable<PostDocument> all = postRepository.findAll();
        System.out.println(all);

        PostDocument postDocument = new PostDocument();
        postDocument.setId(1L);
        postDocument.setTitle("heihie，你好");

        postDocument.setCreated(new Date());

        PostDocument saveDoc = postRepository.save(postDocument);

        System.out.println(saveDoc.toString());
    }

}

