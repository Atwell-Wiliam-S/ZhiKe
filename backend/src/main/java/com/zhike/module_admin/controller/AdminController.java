package com.zhike.module_admin.controller;

import com.zhike.common.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/users")
    public Result<?> getUsers() {
        return Result.success("用户列表");
    }

    @PostMapping("/users")
    public Result<?> createUser() {
        return Result.success("用户创建成功");
    }

    @PutMapping("/users/{id}")
    public Result<?> updateUser(@PathVariable Long id) {
        return Result.success("用户更新成功");
    }

    @DeleteMapping("/users/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        return Result.success("用户删除成功");
    }

    @GetMapping("/roles")
    public Result<?> getRoles() {
        return Result.success("角色列表");
    }

    @GetMapping("/logs")
    public Result<?> getOperationLogs() {
        return Result.success("操作日志");
    }

}