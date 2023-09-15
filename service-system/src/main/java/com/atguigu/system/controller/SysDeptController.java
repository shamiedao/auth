package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysDept;
import com.atguigu.system.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("部门管理")
@RestController
@RequestMapping("/admin/system/sysDept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @PreAuthorize("hasAuthority('bnt.sysDept.list')")
    @ApiOperation("查询部门信息")
    @GetMapping("/findNodes")
    public Result findNodes(){
        List<SysDept> list = sysDeptService.getDepts();
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysDept.remove')")
    @DeleteMapping("/remove/{id}")
    @ApiOperation("删除信息")
    public Result remove(@PathVariable Long id){
        boolean removeById = sysDeptService.deleteById(id);
        return removeById ? Result.ok() : Result.fail();
    }

    @PreAuthorize("hasAuthority('bnt.sysDept.update')")
    @PutMapping("/update")
    @ApiOperation("更新信息")
    public Result update(@RequestBody SysDept sysDept){
        sysDept.setUpdateTime(null);
        boolean updateById = sysDeptService.updateById(sysDept);
        return updateById? Result.ok():Result.fail();
    }

    @ApiOperation("新增信息")
    @PreAuthorize("hasAuthority('bnt.sysDept.add')")
    @PostMapping("/save")
    public Result add(@RequestBody SysDept sysDept){
        boolean save = sysDeptService.save(sysDept);
        return save? Result.ok():Result.fail();
    }

    @ApiOperation("更新状态")
    @PreAuthorize("hasAuthority('bnt.sysDept.update')")
    @GetMapping("/updateStatus/{id}/{status}")
    public Result update (@PathVariable Long id,@PathVariable Integer status){
        Integer integer = sysDeptService.updateButtonStatus(id, status);
        return integer!=null? Result.ok():Result.fail();
    }
}
