package org.example.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptorUtil
{
    private static volatile PasswordEncryptorUtil instance;

    private PasswordEncryptorUtil() {}

    public static PasswordEncryptorUtil getInstance() {
        if (instance == null) {
            synchronized (PasswordEncryptorUtil.class) {
                if (instance == null) {
                    instance = new PasswordEncryptorUtil();
                }
            }
        }
        return instance;
    }

    // 使用BCrypt加密密码
    // 生成盐值并加密密码
    public String EncryptPassword(String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    // 验证用户输入的密码是否与加密密码匹配
    public boolean CheckPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
