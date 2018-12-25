package com.homework.search.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

@Document(indexName = "post", type = "post")
@Data
public class PostDocument implements Serializable {

    @Id
    @Field(type = FieldType.keyword)
    private Long id;

    @Field(type = FieldType.text)
    private String title;

    private Long authorId;
    private String author;
    private String avatar;

    private Long categoryId;
    private String category;

    private Boolean recommend;
    private Integer level;


    private Integer CommentCount;
    private Integer viewCount;

    private Date created;

    private Integer status;

}
