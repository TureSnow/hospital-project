package com.example.hospital.dto;
import com.example.hospital.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * SpringSecurity需要的用户详情
 * Created by macro on 2018/4/26.
 */
public class MyUserDetails implements UserDetails {
    private User user;
    private List<GrantedAuthority> permissionList;
    public MyUserDetails(User user, List<GrantedAuthority> permissionList) {
        this.user = user;
        this.permissionList = permissionList;
    }

    public String getArea(){
        return user.getArea();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户的权限
        return permissionList;
    }

    public int getId(){
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
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