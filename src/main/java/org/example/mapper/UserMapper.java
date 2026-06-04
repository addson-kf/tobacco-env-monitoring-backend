package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.User;

import java.util.List;

@Mapper
public interface UserMapper
{
    // 注册用户
    int createUser(@Param("account") String account,
                    @Param("password") String password,
                    @Param("isAdmin") boolean isAdmin);

    // 删除用户（通过更新 IsDelete 字段）
    int deleteUser(@Param("userId") int userId);

    // 更新用户密码
    int updatePassword(@Param("userId") int userId, @Param("password") String password);

    // 将用户更新为管理员
    int updateToAdmin(@Param("userId") int userId);

    // 查询所有非删除用户
    List<User> findAllUsers();

    // 根据用户 ID 查找用户
    User findUserById(@Param("userId") int userId);

    // 根据账户查找用户
    User findUserByAccount(@Param("account") String account);

    // 恢复已删除角色
    int restoreUser(int userId);

    // 查询用户是否已删除（通过账号）
    boolean isUserDeletedByAccount(String account);
}
