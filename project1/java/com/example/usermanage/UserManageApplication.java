package com.example.usermanage;

import com.example.usermanage.entity.User;
import com.example.usermanage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SpringBootApplication
public class UserManageApplication {


    public static void main(String[] args) {
        SpringApplication.run(UserManageApplication.class, args);
    }

}
