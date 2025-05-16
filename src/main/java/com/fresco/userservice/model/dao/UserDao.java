package com.fresco.userservice.model.dao;

import com.fresco.userservice.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("user")
public class UserDao {

    @Id
    private String id;

    @Field("fullName")
    private String fullName;

    @Field("email")
    private String email;

    @Field("mobileNumber")
    private String mobileNumber;

    @Field("password")
    private String password;

    @Field("isVerified")
    private boolean isVerified;

    @Field("role")
    private UserRole role;

    @Field("deletedAt")
    private Long deletedAt;

    // Add fields like OTP, status, etc. if needed for verification and flow
}
