package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.common.util.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @ApiOperation("保存用户")
    @PostMapping("/save")
    public Result add(@RequestBody SysUser sysUser){
        //获取用户名进行判断是否在数据库中存在
        String username = sysUser.getUsername();
        //创建QueryWrapper对象
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        //设置查询条件，取值前端传过来的用户名进行校验
        sysUserQueryWrapper.eq("username",username);
        List<SysUser> list = sysUserService.list(sysUserQueryWrapper);
        //判断是否存在该用户
        if(list.size()==0){
            //不存在，添加用户信息
            String password = sysUser.getPassword();
            sysUser.setPassword(MD5.encrypt(password));
            boolean save = sysUserService.save(sysUser);
            return save? Result.ok():Result.fail();
        }else{
            //存在，返回已存在该用户的提示信息
            return Result.build(null, ResultCodeEnum.Duplicate);
        }

    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable Long id){
        boolean remove = sysUserService.removeById(id);
        return remove? Result.ok():Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation("更新用户")
    @PutMapping("/update")
    public Result update(@RequestBody SysUser sysUser){
        sysUser.setUpdateTime(new Date());
        boolean update = sysUserService.updateById(sysUser);
        return update? Result.ok():Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("获取用户")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        SysUser get = sysUserService.getById(id);
        return Result.ok(get);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation("更新员工状态")
    @PutMapping("/updateStatus/{id}/{status}")
    public Result status(@PathVariable Long id,@PathVariable Integer status){
        boolean status1 = sysUserService.updateStatus(id, status);
        return status1? Result.ok():Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("分页获取用户")
    @GetMapping("/byPage/{current}/{size}")
    public Result pages(@PathVariable Long current, @PathVariable Long size,SysUserQueryVo sysUserQueryVo){
        Page<SysUser> sysUserPage = new Page<>(current,size);
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        if(createTimeEnd != null && createTimeEnd != "" && createTimeBegin != null && createTimeBegin != ""){
            sysUserQueryWrapper.ge("create_time",createTimeBegin).le("create_time",createTimeEnd);
        }
//        if(createTimeEnd != null && createTimeEnd != ""){
//            sysUserQueryWrapper.le("create_time",createTimeEnd);
//        }
        if(keyword != null && keyword != ""){
            sysUserQueryWrapper.and(new Consumer<QueryWrapper<SysUser>>() {
                @Override
                public void accept(QueryWrapper<SysUser> sysUserQueryWrapper) {
                    sysUserQueryWrapper.like("username",keyword).or().like("name",keyword).or().like("phone",keyword);
                }
            });
        }
        Page<SysUser> page = sysUserService.page(sysUserPage, sysUserQueryWrapper);
        return Result.ok(page);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("分页获取用户，自定义sql语句")
    @GetMapping("/{current}/{size}")
    public Result getPage(@PathVariable Integer current, @PathVariable Integer size,SysUserQueryVo sysUserQueryVo){
        Page<SysUser> sysUserPage = new Page<>(current, size);
        Page<SysUser> ipage = sysUserService.pages(sysUserPage,sysUserQueryVo);
        return Result.ok(ipage);
    }
}
