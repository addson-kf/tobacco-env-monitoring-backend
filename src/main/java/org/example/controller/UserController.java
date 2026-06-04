package org.example.controller;
import org.example.response.base.InsertResponse;
import org.example.response.base.LoginResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.base.DeleteResponse;
import org.example.response.common.User.UserDetailsListResponse;
import org.example.service.IUserService;
import org.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器，处理用户相关请求。
 */
@RestController
@RequestMapping("#{@apiConfig.user.basePath}")
@CrossOrigin(origins = "*")// 暂时允许所有域名访问
public class UserController
{
    @Autowired
    private IUserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    //@RequestBody 用于 接收 HTTP 请求的 JSON 数据 并将其自动转换为 Java 对象
    /**
     * 用户登录。
     */
    @PostMapping("#{@apiConfig.user.endpoints.login}")
    public LoginResponse login(@RequestBody Map<String, String> loginInfo) {
        return userService.UserLogin(loginInfo.get("account"), loginInfo.get("password"));
    }

    /**
     * 注册新用户。
     */
    @PostMapping("#{@apiConfig.user.endpoints.register}")
    public InsertResponse register(@RequestBody Map<String, Object> userInfo) {
        String account = (String) userInfo.get("account");
        String password = (String) userInfo.get("password");
        boolean isAdmin = userInfo.getOrDefault("isAdmin", false) instanceof Boolean && (Boolean) userInfo.get("isAdmin");
        return userService.registerUser(account, password, isAdmin);
    }

    /**
     * 删除用户（逻辑删除）。
     */
    @DeleteMapping("#{@apiConfig.user.endpoints.delete}")
    public DeleteResponse deleteUser(@PathVariable int userId) {
        return userService.deleteUser(userId);
    }

    /**
     * 修改用户密码。
     */
    @PostMapping("#{@apiConfig.user.endpoints.updatePassword}")
    public UpdateResponse updatePassword(@RequestBody Map<String, String> updateInfo) {
        int userId = Integer.parseInt(updateInfo.get("userId"));
        String newPassword = updateInfo.get("newPassword");
        return userService.updatePassword(userId, newPassword);
    }

    /**
     * 将用户设为管理员。
     */
    @PostMapping("#{@apiConfig.user.endpoints.setAdmin}")
    public UpdateResponse setUserAsAdmin(@PathVariable int userId) {
        return userService.setUserAsAdmin(userId);
    }

    /**
     * 管理员查询所有用户详细信息。
     */
    @GetMapping("#{@apiConfig.user.endpoints.getUsers}")
    public UserDetailsListResponse getAllUsersForAdmin() {
        return userService.getAllUserDetails();
    }

    /**
     * 恢复已逻辑删除的用户账号。
     */
    @PostMapping("#{@apiConfig.user.endpoints.restoreUser}")
    public UpdateResponse restoreUser(@PathVariable int userId) {
        return userService.restoreUserById(userId);
    }

    /**
     * 修改密码（登录后可用）
     */
    @PostMapping("#{@apiConfig.user.endpoints.changePassword}")
    public UpdateResponse changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> info
    ) {
        String token = authHeader.split(" ")[1].trim();
        String username = jwtUtils.getUsernameFromToken(token);
        String oldPassword = info.get("oldPassword");
        String newPassword = info.get("newPassword");
        return userService.changePasswordAfterLogin(username, oldPassword, newPassword);
    }

    /**
     * Token验证接口
     */
    @GetMapping("#{@apiConfig.user.endpoints.verifyToken}")
    public LoginResponse verifyToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.split(" ")[1].trim();
        return userService.verifyToken(token);
    }
}