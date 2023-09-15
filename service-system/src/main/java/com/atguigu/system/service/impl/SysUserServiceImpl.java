package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.helper.RouterHelper;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public Page<SysUser> pages(Page<SysUser> sysUserPage, SysUserQueryVo sysUserQueryVo) {
        return baseMapper.getPages(sysUserPage, sysUserQueryVo);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
//        SysUser sysUser = new SysUser();
//        sysUser.setId(id);
//        sysUser.setStatus(status);
//        boolean updateById = sysUserService.updateById(sysUser);
        return baseMapper.updateStatus(id, status);
    }

//    @Override
//    public Result selectUser(String username, String password) {
//
//        SysUser sysUser = baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
//
//        if (sysUser == null) {
//            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
//        }
//        //验证密码是否正确
//        if (!MD5.encrypt(password).equals(sysUser.getPassword())) {
//            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
//        }
//        //验证用户的状态是否被锁定
//        if (sysUser.getStatus() == 0) {
//            return Result.build(null, ResultCodeEnum.LOCKED);
//        }
//        return null;
//    }


    @Override
    public SysUser selectUser(String username) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public Map<String, Object> getMenusforUser(Long id) {
        //根据用户ID返回不同的菜单项
        List<SysMenu> sysMenuList = getMenufordiffentUser(id);
        //将菜单项树化 将权限菜单通过MenuHelper工具类转换为菜单树
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        //将菜单树通过RoutHelper工具类转换为路由
        List<RouterVo> routerVos = RouterHelper.buildRouters(sysMenus);
        //创建一个map
        Map<String, Object> map = new HashMap<>();
        //将路由放到map中
        map.put("routers", routerVos);
        //将每个用户的权限菜单放到map中
        List<String> buttons = getButtons(id);
        map.put("buttons", buttons);
        return map;
    }

    @Override
    public List<String> getButtons(Long id) {
        //根据用户ID返回不同的菜单项
        List<SysMenu> sysMenuList = getMenufordiffentUser(id);
        //创建一个保存用户的权限按钮标识符的List
        List<String> buttons = new ArrayList<>();
        //遍历所有的权限菜单
        for (SysMenu sysMenu : sysMenuList) {
            //证明当前节点是按钮，获取按钮权限标识符
            if (sysMenu.getType() == 2) {
                String perms = sysMenu.getPerms();
                if (perms != null && perms != "") {
                    //将按钮权限标识符添加到userBtnPerms中
                    buttons.add(perms);
                }
            }
        }
        return buttons;
    }

    //根据用户ID返回不同的菜单项
    List<SysMenu> getMenufordiffentUser(Long id) {
        List<SysMenu> sysMenuList = null;
        //判断该用户是系统管理员还是其他管理员
        if (id == 1) {
            //返回所有的菜单项
            sysMenuList = sysMenuMapper.selectList(new QueryWrapper<SysMenu>().eq("status", 1));
        } else {
            sysMenuList = sysMenuMapper.getMenusByUserId(id);
        }
        return sysMenuList;
    }
}
