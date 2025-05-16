package com.fresco.userservice.model.dao;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;


@Data
public class BaseDao {

    @Field("createdAt")
    private Timestamp createdAt;

    @Field("updatedAt")
    private Timestamp updatedAt;
}
