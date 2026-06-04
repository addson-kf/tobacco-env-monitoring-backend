package org.example.service;
import org.example.response.base.LoginResponse;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.User.UserDetailsListResponse;

/**
 * 用户业务逻辑层接口。
 */
public interface IUserService
{
    /**
     * 用户账号密码登录。
     *
     * @param account 账号
     * @param password 密码
     * @return 登录结果响应
     */
    LoginResponse UserLogin(String account, String password);

    /**
     * 注册新用户。
     *
     * @param account 账号
     * @param password 密码
     * @param isAdmin 是否为管理员
     * @return 插入操作结果响应
     */
    InsertResponse registerUser(String account, String password, boolean isAdmin);

    /**
     * 删除指定 ID 的用户（逻辑删除）。
     *
     * @param userId 用户 ID
     * @return 删除操作结果响应
     */
    DeleteResponse deleteUser(int userId);

    /**
     * 更新指定用户的密码。
     *
     * @param userId 用户 ID
     * @param newPassword 新密码
     * @return 更新操作结果响应
     */
    UpdateResponse updatePassword(int userId, String newPassword);

    /**
     * 将用户设置为管理员。
     *
     * @param userId 用户 ID
     * @return 更新操作结果响应
     */
    UpdateResponse setUserAsAdmin(int userId);

    /**
     * 恢复逻辑删除的用户。
     *
     * @param userId 用户 ID
     * @return 更新操作结果响应
     */
    UpdateResponse restoreUserById(int userId);

    /**
     * 获取所有用户详细信息。
     *
     * @return 所有用户详细信息响应
     */
    UserDetailsListResponse getAllUserDetails();

    /**
     * 控制台打印所有用户信息表格。
     */
    void FindAllUsers();

    UpdateResponse changePasswordAfterLogin(String username, String oldPassword, String newPassword);

    // 验证Token有效性并获取用户信息
    LoginResponse verifyToken(String token);


}
