package com.zhike.mapper;

import com.zhike.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRoleMapper {
    @Select("SELECT * FROM user_roles WHERE user_id = #{userId}")
    List<UserRole> selectByUserId(Integer userId);
}
