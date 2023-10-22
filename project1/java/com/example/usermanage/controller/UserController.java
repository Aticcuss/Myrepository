package com.example.usermanage.controller;
import com.example.usermanage.entity.User;
import com.example.usermanage.mapper.UserMapper;
import com.example.usermanage.server.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserServer userServer;
    //修改或者添加新的用户

    @PostMapping("/save")
    public int save(@RequestBody User user){
        System.out.println(user.getPassword());
        return userServer.save(user);
    }
    @DeleteMapping("/del/{userId}")
    public int delUser(@PathVariable int userId){
        return userMapper.delUser(userId);
    }
    @GetMapping("/page")
    public Map<String, Object> findPage(@RequestParam int pageNum,
                                        @RequestParam int pageSize,
                                        @RequestParam String userName,
                                        @RequestParam String nickName,
                                        @RequestParam String userId){
        return userServer.findPage(pageNum,pageSize,userName,nickName,userId);
    }

    @GetMapping("/find")
    public List<User> findById(@RequestParam int userId){
        return userMapper.findPageById(userId);
    }
}
