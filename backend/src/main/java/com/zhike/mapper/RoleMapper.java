package com.zhike.mapper;

import com.zhike.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Select("SELECT * FROM roles")
    List<Role> selectAll();
    
    @Select("SELECT * FROM roles WHERE id = #{id}")
    Role selectById(Integer id);
    
    @Select("SELECT * FROM roles WHERE role_code = #{roleCode}")
    Role selectByRoleCode(String roleCode);
}
