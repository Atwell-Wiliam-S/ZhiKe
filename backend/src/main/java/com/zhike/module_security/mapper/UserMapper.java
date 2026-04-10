package com.zhike.module_security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhike.module_security.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据访问接口
 * 提供用户表的数据库操作，继承MyBatis-Plus的BaseMapper获得基础CRUD功能
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户信息
     * 
     * @param username 用户名
     * @return 用户对象，如果不存在则返回null
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    /**
     * 根据用户ID查询用户信息
     * 
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
}
