package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation("查询信息返回页面")
    @GetMapping("/findMenuNodes")
    public Result findMenuNodes(){
        List<SysMenu> list = sysMenuService.getTreePage();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @ApiOperation("删除菜单项")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){
        Result result = sysMenuService.deleteById(id);
        return result;
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @ApiOperation("添加菜单项")
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){
        boolean save = sysMenuService.save(sysMenu);
        return save? Result.ok():Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation("根据id获取菜单项")
    @GetMapping("/getById/{id}")
    public Result getById(@PathVariable Long id){
        SysMenu sysMenu = sysMenuService.getById(id);
        return Result.ok(sysMenu);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @ApiOperation("更新菜单项")
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu){
        sysMenu.setUpdateTime(null);
        boolean updateById = sysMenuService.updateById(sysMenu);
        return updateById? Result.ok():Result.fail();
    }


    @ApiOperation("根据角色id查询已分配权限")
    @GetMapping("/getRoleMenuList/{id}")
    public Result getRoleMenuList(@PathVariable Long id){
        //返回所有权限数据以及角色id对应的相应权限
        List<SysMenu> sysMenus = sysMenuService.getAuthpriority(id);
        return Result.ok(sysMenus);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation("给角色分配权限")
    @PostMapping("/assignMenu")
    public Result assignMenu(@RequestBody AssignMenuVo assignMenuVo){
        sysMenuService.deployAuthpriority(assignMenuVo);
        return Result.ok();
    }
}
