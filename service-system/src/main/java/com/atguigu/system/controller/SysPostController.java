package com.atguigu.system.controller;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysPost;
import com.atguigu.model.vo.SysPostQueryVo;
import com.atguigu.system.service.SysPostService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("岗位管理")
@RestController
@RequestMapping("/admin/system/sysPost")
public class SysPostController {

    @Autowired
    private SysPostService sysPostService;

    @ApiOperation("查询信息")
    @GetMapping("/{page}/{list}")
    @PreAuthorize("hasAuthority('bnt.sysPost.list')")
    public Result list(@PathVariable Integer page, @PathVariable Integer list, SysPostQueryVo sysPostQueryVo){
        Page<SysPost> postPage = new Page<>(page, list);
        Boolean status = sysPostQueryVo.getStatus();
        String name = sysPostQueryVo.getName();
        String postCode = sysPostQueryVo.getPostCode();
        QueryWrapper<SysPost> sysPostQueryWrapper = new QueryWrapper<>();
        if(name != null){
            sysPostQueryWrapper.like("name",name);
        }
        if(postCode != null){
            sysPostQueryWrapper.like("post_code",postCode);
        }
        Page<SysPost> ipage = sysPostService.page(postPage, sysPostQueryWrapper);
        return Result.ok(ipage);
    }
}
