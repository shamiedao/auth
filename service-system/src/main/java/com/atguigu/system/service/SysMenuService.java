package com.atguigu.system.service;

import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getTreePage();

    Result deleteById(Long id);

    List<SysMenu> getAuthpriority(Long id);

    void deployAuthpriority(AssignMenuVo assignMenuVo);
}
