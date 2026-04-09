package com.zhike.mapper;

import com.zhike.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username}")
    User selectByUsername(String username);
    
    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(Integer id);
    
    @Select("SELECT u.*, r.* FROM users u LEFT JOIN roles r ON u.role_id = r.id WHERE u.username = #{username}")
    User selectUserWithRole(String username);
}
