package com.ricky.campus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ricky.campus.mapper.AdminKeyMapper;
import com.ricky.campus.model.entity.AdminKey;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class AdminService {

    @Resource
    private AdminKeyMapper adminKeyMapper;

    /**
     * 校验密钥是否有效
     * 逻辑：查询 admin_key 表，secretKey 匹配 + status=1 + expireTime > 当前时间
     * TODO: 由用户完善校验逻辑
     */
    public boolean validateKey(String secretKey) throws RuntimeException {
        LambdaQueryWrapper<AdminKey> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminKey::getSecretKey, secretKey)
                .eq(AdminKey::getStatus, 1)
                .gt(AdminKey::getExpireTime,LocalDateTime.now());
        int contains = Math.toIntExact(adminKeyMapper.selectCount(wrapper));
        if (contains <= 0) {
            throw new RuntimeException();
        }
        return true;
    }
}
