package com.example.usermanage.mapper;

import com.example.usermanage.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface UserMapper {
    @Insert("INSERT INTO t_users(username,nickname,password,phone,privileges,state) VALUES(#{userName},#{userName},#{password},#{phone},0,0)")
    int insertUser(User user);
    @Update("update t_users set nickname=#{nickName},phone=#{phone},privileges=#{privileges},state=#{state} " +
            "where user_id=#{userId}")
    int updateUser(User user);
    @Delete("delete from t_users " +
            "where user_id=#{userId}")
    int delUser(@Param("userId") int userId);
    @Select("Select * from t_users " +
            "where username like  #{userName} and nickname like #{nickName} limit #{pageNum},#{pageSize}")
    List<User> findPage(int pageNum, int pageSize,String userName,String nickName,String userId);

    @Select("Select * from t_users " +
            "where user_id=#{userId}")
    List<User> findPageById(int userId);
    @Select("SELECT count(*) from t_users " +
            "where username like #{userName} and nickname like #{nickName}")
    int countUser(String userName,String nickName);
}
