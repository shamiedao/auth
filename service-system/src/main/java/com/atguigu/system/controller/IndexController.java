package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "登录管理")
@RequestMapping("/admin/system/index")
@RestController
public class IndexController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private SysUserService sysUserService;

//    @ApiOperation("登录")
//    @PostMapping("/login")
//    public Result login(){
//        //需要返回的数据 {"code":200,"data":{"token":"admin-token"}}
//        //创建一个Map
//        Map map=new HashMap<>();
//        map.put("token","admin");
//        return Result.ok(map);
//    }

    @ApiOperation("连接数据库的登录")
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        //获取用户名信息
        String username = loginVo.getUsername();
        //获取密码
        String password = loginVo.getPassword();
        //查询数据库是否存在该用户
        SysUser sysUser = sysUserService.selectUser(username);
        if (sysUser == null) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }
        //验证密码是否正确
        if (!MD5.encrypt(password).equals(sysUser.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }
        //验证用户的状态是否被锁定
        if (sysUser.getStatus() == 0) {
            return Result.build(null, ResultCodeEnum.LOCKED);
        }
        //创建一个Map
        //使用UUID生成随机的token
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        Map map = new HashMap<>();
        map.put("token", token);
        //将用户信息保存到redis里,并设置有效时间为两个小时
        redisTemplate.boundValueOps(token).set(sysUser, 2, TimeUnit.HOURS);
        return Result.ok(map);
    }

//    @ApiOperation("连接数据库的登录,业务逻辑判断在service层")
//    @PostMapping("/login")
//    public Result login(@RequestBody LoginVo loginVo) {
//        //获取用户名信息
//        String username = loginVo.getUsername();
//        //获取密码
//        String password = loginVo.getPassword();
//        //查询数据库是否存在该用户
//        Result result = sysUserService.selectUser(username, password);
//        if (result == null) {
//            //创建一个Map
//            //使用UUID生成随机的token
//            String token = UUID.randomUUID().toString().replaceAll("-", "");
//            Map map = new HashMap<>();
//            map.put("token", token);
//              //将用户信息保存到redis里,并设置有效时间为两个小时
//              redisTemplate.boundValueOps(token).set(sysUser, 2, TimeUnit.HOURS);
//            return Result.ok(map);
//        } else {
//            return result;
//        }
//    }

    //todo 看前端发送请求的方式？？？ 点击登录发送量个请求？？？  每次请求数据还会携带token？？？
    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");
        SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
        //根据用户id获取用户权限菜单的方法
        Map<String,Object> map=sysUserService.getMenusforUser(sysUser.getId());
//        Map map = new HashMap<>();
        //设置用户名
        map.put("name", sysUser.getUsername());
        //设置头像
        map.put("avatar", sysUser.getHeadUrl());
        return Result.ok(map);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        redisTemplate.delete(token);
        return Result.ok();
    }
}
