package com.example.domain;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 UserDetails是接口，这里是定义UserDetails的实现类
 */
public class LoginUser implements UserDetails {

    private User user;

    //存储权限信息、多参构造函数;
    private List<String> permissions;
    public LoginUser(User user,List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    //定义存储权限信息的集合
    @JSONField(serialize = false)
    private List<GrantedAuthority> authorities; //因为其不支持序列化,serialize = false排除序列化


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //首先判断权限信息是否存在, 存在直接返回;
        if(authorities!=null){ return authorities; }
        //不存在, 根据 UserDetailsService 获取用户信息\权限,传递过来的数据进行解析生成;
        //JAVA8 Stream API 将用户权限集合string数据转换为: GrantedAuthority类型集合,
        //GrantedAuthority 对象：用于表示用户被授予的权限或角色,构造函数接受一个字符串参数
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

