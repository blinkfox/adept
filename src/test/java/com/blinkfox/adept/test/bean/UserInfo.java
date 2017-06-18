package com.blinkfox.adept.test.bean;

/**
 * 用户信息Bean.
 * @author blinkfox on 2017/6/13.
 */
public class UserInfo {

    /* 唯一标识 */
    private String id;

    /* 姓名name */
    private String name;

    /* 昵称 */
    private String nickName;

    /* 邮箱 */
    private String email;

    /* 性别 */
    private int sex;

    /* 生日 */
    private String birthday;

    /**
     * 空构造方法.
     */
    public UserInfo() {
        super();
    }

    /* getter和setter方法. */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * toString方法.
     * @return String
     */
    @Override
    public String toString() {
        return "bean{"
                + "id='" + id + '\''
                + ", name='" + name + '\''
                + ", nickName='" + nickName + '\''
                + ", email='" + email + '\''
                + ", sex=" + sex
                + ", birthday=" + birthday
                + '}';
    }

}