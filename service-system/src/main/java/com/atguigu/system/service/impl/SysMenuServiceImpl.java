package com.atguigu.system.service.impl;

import com.atguigu.common.helper.MenuHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.system.exception.GuiguException;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysRoleMenuMapper;
import com.atguigu.system.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper,SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> getTreePage() {
        //调用SysMenuMapper中获取所有的方法
        List<SysMenu> list = sysMenuService.list(new QueryWrapper<SysMenu>().eq("status",1));
        //通过MenuHelper工具类将菜单集合转换为菜单树
        List<SysMenu> sysMenus = MenuHelper.buildTree(list);
        return sysMenus;
    }

    @Override
    public Result deleteById(Long id) {
        //根据id查询当前节点是否有子节点
        List<SysMenu> menu = sysMenuService.list(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if(menu.size()>0){
            //todo 已经定义了全局的异常处理，捕获异常后返回的也是Result对象
            //抛出异常
            throw new GuiguException(ResultCodeEnum.NODES);
//            return Result.build(null,ResultCodeEnum.NODES);
        }else{
            sysMenuService.removeById(id);
            return Result.ok();
        }
    }

    @Override
    public List<SysMenu> getAuthpriority(Long id) {
        //查询所有权限菜单项
        List<SysMenu> sysMenuList = sysMenuService.list(new QueryWrapper<SysMenu>().eq("status",1));
        //根据角色id查询相应的权限
        List<SysRoleMenu> roleMenuList = sysRoleMenuMapper.selectList(new QueryWrapper<SysRoleMenu>().eq("role_id", id));
        //获取角色对应的菜单id
        List<Long> collect = roleMenuList.stream().map(sysRoleMenu -> sysRoleMenu.getMenuId()).collect(Collectors.toList());
        //遍历所有的权限菜单
        for (SysMenu sysMenu : sysMenuList) {
            Long sysMenuId = sysMenu.getId();
            //当角色id对应的菜单id在权限菜单存在时，当前权限设置为true
            if(collect.contains(sysMenuId)){
                sysMenu.setSelect(true);
            }else{
                sysMenu.setSelect(false);
            }
        }
        //将数据转换为菜单树
        List<SysMenu> sysMenus = MenuHelper.buildTree(sysMenuList);
        return sysMenus;
    }

    @Override
    public void deployAuthpriority(AssignMenuVo assignMenuVo) {
        //根据角色id删除现有权限
        Long roleId = assignMenuVo.getRoleId();
        sysRoleMenuMapper.delete(new QueryWrapper<SysRoleMenu>().eq("role_id",roleId));
        //获取前台分配的新权限
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        if(menuIdList != null && menuIdList.size()>0){
            //给角色分配现有权限
            for (Long menuId : menuIdList) {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenu.setMenuId(menuId);
                //添加新权限
                sysRoleMenuMapper.insert(sysRoleMenu);
            }
        }
    }
}
