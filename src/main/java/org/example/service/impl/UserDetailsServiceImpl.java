package org.example.service.impl;

import org.example.model.User;
import org.example.dao.impl.UserDaoImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserDaoImpl userDAO;

    public UserDetailsServiceImpl(UserDaoImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 通过 DAO 查找用户（你的 UserDaoImpl 有 findUserByAccount 方法）
        User user = userDAO.findUserByAccount(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return user;
    }
}
