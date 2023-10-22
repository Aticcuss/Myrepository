package com.example.usermanage.entity;
public class User {
    private int user_id;
    private String userName;
    //后台返回数据时忽略passwrod
    private String password;
    private String nickName;
    private String phone;
    private int privileges;
    private int state;
    public int getUserId() {
        return user_id;
    }
    public void setUserId(int userId) {
        this.user_id = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public int getPrivileges() {
        return privileges;
    }
    public void setPrivileges(int privileges) {
        this.privileges = privileges;
    }
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
}
