package org.example.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Collection;


public class User implements UserDetails
{
    private int userId;
    private String account;
    private String password;
    private boolean admin;
    private boolean delete;



    //Jackson 在反序列化 JSON 时需要一个默认构造函数来实例化对象:即这个无参构造方法
    public User() {}


    public User(int userId, String account, String password, boolean admin, boolean isDelete) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.admin = admin;
        this.delete = isDelete;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean isDelete) {
        this.delete = isDelete;
    }

    //输出所有字段用于测试
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + admin +
                ", isDelete=" + delete +
                '}';
    }

    /**
     * 获取用户权限（角色）：根据 admin 字段返回 ROLE_ADMIN 或 ROLE_USER
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = admin ? "ROLE_ADMIN" : "ROLE_USER";
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() { return account; }         // 返回账号（作为用户名）

    // 以下方法处理账号状态（根据业务逻辑返回）：
    @Override
    public boolean isAccountNonExpired() { return true; }    // 账号未过期（假设无过期机制）
    @Override
    public boolean isAccountNonLocked() { return true; }      // 账号未锁定（假设无锁定机制）
    @Override
    public boolean isCredentialsNonExpired() { return true; } // 凭证未过期（假设密码永不过期）
    @Override
    public boolean isEnabled() { return !delete; }           // 未被逻辑删除的用户视为启用
}
