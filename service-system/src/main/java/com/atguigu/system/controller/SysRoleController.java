package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.exception.GuiguException;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;


    @GetMapping("/testException")
    public List<SysRole> findAll(){
        try {
            //int i=10/0;
        } catch (Exception e) {
            throw new GuiguException(222,"出现了自定义异常");
        }
        List<SysRole> list = sysRoleService.list();
        return list;
    }

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/getAllRoles")
    public Result getAll() {
        List<SysRole> list = sysRoleService.list();
//        list.forEach(System.out::println);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色")
    @PostMapping("/save")
    public Result insertSysRole(@RequestBody SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除角色")
    @DeleteMapping("/removeRoleById/{id}")
    public Result deleteRole(@PathVariable Long id) {
        boolean remove = sysRoleService.removeById(id);
        if (remove) {
            return Result.ok();
        } else {
            return Result.fail();

        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("根据id更新角色")
    @PutMapping("/updateRoleById")
    public Result updateRole(@RequestBody SysRole sysRole) {
        //设置重置更新时间
        //一
        sysRole.setUpdateTime(new Date());
        //二  消除sql语句中的set UpdateTime
//        sysRole.setUpdateTime(null);
        boolean update = sysRoleService.updateById(sysRole);
        if (update) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询角色")
    @GetMapping("/getRoleById/{id}")
    public Result selectOne(@PathVariable Long id) {
        SysRole sysRole = sysRoleService.getById(id);
        return Result.ok(sysRole);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除")
    @DeleteMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Long> ids) {
        boolean removeByIds = sysRoleService.removeByIds(ids);
        if (removeByIds) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("分页查询角色信息,使用定义好的sql语句")
    @GetMapping("/getPageByMyBatisPlus/{current}/{size}")
    public Result getPageByMyBatisPlus(@PathVariable Long current, @PathVariable Long size, SysRoleQueryVo sysRoleQueryVo){
        //创建一个Page对象
        Page<SysRole> sysRolePage = new Page<>(current,size);
        //创建QueryWrapper对象
        QueryWrapper<SysRole> sysRoleQueryWrapper = new QueryWrapper<>();
        //获取查询条件
        String roleName = sysRoleQueryVo.getRoleName();
        //判断查询条件是否为空
        if(roleName != null && roleName != ""){
            //封装查询条件
            sysRoleQueryWrapper.like("role_name",roleName);
        }
        //调用SysRoleService中自带的分页的方法
        Page<SysRole> iPage = sysRoleService.page(sysRolePage, sysRoleQueryWrapper);
        return Result.ok(iPage);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("分页查询角色信息,使用自定义sql语句")
    @GetMapping("/{current}/{size}")
    public Result getPage(
            @ApiParam(name = "current",value = "当前页",required = true)
            @PathVariable Long current,
            @ApiParam(name = "size",value = "每页显示的记录数",required = true)
            @PathVariable Long size,
            @ApiParam(name = "roleQueryVo",value = "查询条件",required = false)
            SysRoleQueryVo sysRoleQueryVo){
        //创建一个Page对象
        Page<SysRole> sysRolePage = new Page<>(current, size);
        //调用SysRoleService中分页及带条件查询的方法
        IPage<SysRole> iPage = sysRoleService.findPage(sysRolePage,sysRoleQueryVo);
        return Result.ok(iPage);
    }

    @ApiOperation("用户已分配角色信息以及所有的角色信息")
    @GetMapping("/getRolesByUserId/{id}")
    public Result getRolesByUserId(@PathVariable Long id){
        Map<String,Object> map=sysRoleService.getRoles(id);
        return Result.ok(map);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("给用户分配角色")
    @PostMapping("/assignRoles")
    public Result assignRoles(@RequestBody AssignRoleVo assignRoleVo){
        sysRoleService.updateRoles(assignRoleVo);
        return Result.ok();
    }


}
