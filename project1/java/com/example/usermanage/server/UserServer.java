package com.example.usermanage.server;
import com.example.usermanage.entity.User;
import com.example.usermanage.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Service
public class UserServer {
    @Autowired
    private UserMapper userMapper;
    public int save(User user){
        //判断是更新用户信息还是插入新的用户
        try {
            if (user.getUserId() > 0) {
                return userMapper.updateUser(user);
            } else {
                return userMapper.insertUser(user);
            }
        }catch (Exception e){
            System.out.println("用户已存在");
            return 0;
        }
    }
    public Map<String, Object> findPage(int pageNum,int pageSize,String userName,String nickName,String userId){
        //计算页开始的编号
        Map<String, Object> res = new HashMap<>();
        List<User> data=new ArrayList<>();
        int total = 0;
        //判断userId是否为空，如果不为空则根据userId进行查找
        try{
            if(!userId.equals("")){
                int id = Integer.parseInt(userId);
                data=userMapper.findPageById(id);
                if(!data.isEmpty()){
                    total=1;
                }
            }else {
                //计算开始行编号
                pageNum=(pageNum-1)*pageSize;
                userName= "%"+userName+"%";
                nickName= "%"+nickName+"%";
                total=userMapper.countUser(userName,nickName);
                data = userMapper.findPage(pageNum,pageSize,userName,nickName,userId);
            }
        }catch (NumberFormatException e){
            //userId不能转换为整形
            System.out.println("UserId is not a integer");
        }
        //data为用户信息
        res.put("data",data);
        //total为查询结果的用户总数
        res.put("total",total);
        return res;
    }
}
