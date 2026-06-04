package org.example.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.example.dao.impl.UserDaoImpl;
import org.example.dto.UserDTO;
import org.example.dto.UserDetailsListDTO.UserDetailsDTO;
import org.example.dto.UserDetailsListDTO.UserDetailsListDTO;
import org.example.model.User;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.LoginResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.User.UserDetailsListResponse;
import org.example.service.IUserService;
import org.example.util.PasswordEncryptorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.example.util.JwtUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    private UserDaoImpl userDAO;

    @Autowired
    private JwtUtils jwtUtils;


    /**
     * 用户登录功能，验证账号是否存在、是否被逻辑删除，以及密码是否正确。
     *
     * @param account  用户账号
     * @param password 用户密码（明文）
     * @return 登录响应结果，包含用户信息或失败原因
     */
    @Transactional
    public LoginResponse UserLogin(String account, String password) {
        try {
            User user = userDAO.findUserByAccount(account);
            if (user == null) {
                return new LoginResponse(false, "用户不存在", null,null);
            }
            if (user.isDelete()) {
                return new LoginResponse(false, "该账户已被删除，请联系管理员", null,null);
            }

            if (!PasswordEncryptorUtil.getInstance().CheckPassword(password, user.getPassword())) {
                return new LoginResponse(false, "用户名或密码错误", null,null);
            }

            // 生成 JWT Token（User 已实现 UserDetails，可直接作为 userDetails）
            UserDetails userDetails = user;
            String token = jwtUtils.generateToken(userDetails);

            UserDTO userDTO = new UserDTO(user.getUserId(), user.getAccount(), user.isAdmin());

            System.out.printf(user.getUserId() +" " +  user.getAccount() + " "+ user.isAdmin());
            return new LoginResponse(true, "登录成功", userDTO,token);
        } catch (Exception e) {
            return new LoginResponse(false, "登录异常：" + e.getMessage(), null,null);
        }
    }


    /**
     * 注册新用户。会校验账号是否重复，密码进行加密后保存。
     *
     * @param account  用户账号
     * @param password 用户密码（明文）
     * @param isAdmin  是否为管理员账号
     * @return 插入响应结果
     */
    @Transactional
    public InsertResponse registerUser(String account, String password, boolean isAdmin) {
        try {
            if (userDAO.findUserByAccount(account) != null) {
                return new InsertResponse(false, "账号已存在");
            }
            String hashedPassword = PasswordEncryptorUtil.getInstance().EncryptPassword(password);
            userDAO.createUser(account, hashedPassword, isAdmin);
            return new InsertResponse(true, "注册成功");
        } catch (Exception e) {
            return new InsertResponse(false, "注册失败：" + e.getMessage());
        }
    }


    /**
     * 删除用户（逻辑删除），通过 userId 查找用户并设置删除状态。
     *
     * @param userId 用户 ID
     * @return 删除响应结果
     */
    @Transactional
    public DeleteResponse deleteUser(int userId) {
        try {
            User user = userDAO.findUserById(userId);
            if (user == null) {
                return new DeleteResponse(false, "用户不存在");
            }
            userDAO.deleteUser(userId);
            return new DeleteResponse(true, "用户已删除");
        } catch (Exception e) {
            return new DeleteResponse(false, "删除失败：" + e.getMessage());
        }
    }


    /**
     * 修改用户密码。通过 userId 查找用户并更新加密后的密码。
     *
     * @param userId      用户 ID
     * @param newPassword 新密码（明文）
     * @return 更新响应结果
     */
    @Transactional
    public UpdateResponse updatePassword(int userId, String newPassword) {
        try {
            User user = userDAO.findUserById(userId);
            if (user == null) {
                return new UpdateResponse(false, "用户不存在");
            }
            userDAO.updatePassword(userId, PasswordEncryptorUtil.getInstance().EncryptPassword(newPassword));
            return new UpdateResponse(true, "密码更新成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "密码更新失败：" + e.getMessage());
        }
    }

    /**
     * 将普通用户设为管理员账号。
     *
     * @param userId 用户 ID
     * @return 更新响应结果
     */
    @Transactional
    public UpdateResponse setUserAsAdmin(int userId) {
        try {
            User user = userDAO.findUserById(userId);
            if (user == null) {
                return new UpdateResponse(false, "用户不存在");
            }
            userDAO.updateToAdmin(userId);
            return new UpdateResponse(true, "用户已设为管理员");
        } catch (Exception e) {
            return new UpdateResponse(false, "设置管理员失败：" + e.getMessage());
        }
    }

    /**
     * 控制台打印所有用户信息。仅用于调试和查看数据表内容。
     */
    @Transactional
    public void FindAllUsers() {
        try {
            List<User> users = userDAO.findAllUsers();
            int idWidth = 6;
            int accountWidth = 20;
            int passwordWidth = 40;
            int adminWidth = 8;
            int deleteWidth = 8;
            int totalWidth = idWidth + accountWidth + passwordWidth + adminWidth + deleteWidth + 15;

            String tableName = " Users ";
            System.out.println("+" + "-".repeat(totalWidth - 2) + "+");
            System.out.printf("|%s%s%s|\n",
                    " ".repeat((totalWidth - tableName.length()) / 2),
                    tableName,
                    " ".repeat((totalWidth - tableName.length()) / 2 - 1));
            System.out.println("+" + "-".repeat(totalWidth - 2) + "+");

            String headerFormat = "| %-6s | %-20s | %-40s | %-8s | %-8s |\n";
            System.out.printf(headerFormat, "ID", "Account", "Password", "Admin", "Delete");
            System.out.println("+" + "-".repeat(totalWidth - 2) + "+");

            for (User user : users) {
                String password = user.getPassword();
                if (password.length() > passwordWidth - 3) {
                    password = password.substring(0, passwordWidth - 6) + "...";
                }

                System.out.printf(headerFormat, user.getUserId(), user.getAccount(), password, user.isAdmin(),
                        user.isDelete());
            }

            System.out.println("+" + "-".repeat(totalWidth - 2) + "+");
        } catch (Exception e) {
            System.out.println("查询用户失败：" + e.getMessage());
        }
    }


    /**
     * 获取所有用户的详细信息列表，用于管理员查看。
     *
     * @return 用户详情列表响应对象
     */
    @Transactional
    public UserDetailsListResponse getAllUserDetails() {
        try {
            List<User> users = userDAO.findAllUsers();
            List<UserDetailsDTO> userDTOList = users.stream()
                    .map(user -> new UserDetailsDTO(
                            user.getUserId(),
                            user.getAccount(),
                            user.getPassword(),
                            user.isAdmin(),
                            user.isDelete()
                    ))
                    .collect(Collectors.toList());

            UserDetailsListDTO data = new UserDetailsListDTO(userDTOList);
            return new UserDetailsListResponse(true, "查询所有用户成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserDetailsListResponse(false, "查询所有用户失败", new UserDetailsListDTO());
        }
    }

    /**
     * 恢复被逻辑删除的用户账号。
     *
     * @param userId 用户 ID
     * @return 更新响应结果
     */
    @Transactional
    public UpdateResponse restoreUserById(int userId) {
        try {
            userDAO.restoreUser(userId);
            return new UpdateResponse(true, "用户恢复成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "用户恢复失败：" + e.getMessage());
        }
    }

    @Transactional
    public UpdateResponse changePasswordAfterLogin(String username, String oldPassword, String newPassword) {
        try {
            // 验证用户存在且未被删除
            User user = userDAO.findUserByAccount(username);
            if (user == null || user.isDelete()) {
                return new UpdateResponse(false, "用户不存在或已被删除");
            }

            // 验证旧密码
            if (!PasswordEncryptorUtil.getInstance().CheckPassword(oldPassword, user.getPassword())) {
                return new UpdateResponse(false, "旧密码错误");
            }

            // 更新密码
            userDAO.updatePassword(user.getUserId(),
                    PasswordEncryptorUtil.getInstance().EncryptPassword(newPassword));

            return new UpdateResponse(true, "密码更新成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "密码更新失败：" + e.getMessage());
        }
    }

    /**
     * 优化Token验证逻辑（添加刷新机制）
     */
    @Override
    public LoginResponse verifyToken(String token) {
        try {
            // 解析Token
            String username = jwtUtils.getUsernameFromToken(token);
            User user = userDAO.findUserByAccount(username);

            // 检查用户有效性
            if (user == null || !user.isEnabled()) {
                return new LoginResponse(false, "用户不存在或已被删除", null, null);
            }

            // 验证Token签名和过期时间
            if (!jwtUtils.validateToken(token, user)) {
                return new LoginResponse(false, "无效的Token", null, null);
            }

            // 生成新Token（刷新机制）
            String newToken = jwtUtils.generateToken(user);

            // 返回用户信息和新Token
            UserDTO userDTO = new UserDTO(user.getUserId(), user.getAccount(), user.isAdmin());
            return new LoginResponse(true, "Token验证成功", userDTO, newToken);

        } catch (ExpiredJwtException e) {
            return new LoginResponse(false, "Token已过期", null, null);
        } catch (JwtException e) {
            return new LoginResponse(false, "无效的Token", null, null);
        } catch (Exception e) {
            return new LoginResponse(false, "Token验证失败：" + e.getMessage(), null, null);
        }
    }
}
