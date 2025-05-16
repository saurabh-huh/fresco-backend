package com.fresco.userservice.model.dao;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;

@MappedSuperclass
@Data
public class BaseDao {

    @Field("createdAt")
    private Timestamp createdAt;

    @Field("updatedAt")
    private Timestamp updatedAt;
}
