package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.mapper.SysUserRoleMapper;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public IPage<SysRole> findPage(Page<SysRole> sysRolePage, SysRoleQueryVo sysRoleQueryVo) {
        return baseMapper.selectByPage(sysRolePage, sysRoleQueryVo);
    }

    @Override
    public Map<String, Object> getRoles(Long id) {
        //获取所有的角色信息
        List<SysRole> sysRoles = baseMapper.selectList(null);
        //根据用户id获取该用户已经分配的角色信息
        List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", id));
        //获取返回对象的角色id
        List<Long> collect = sysUserRoles.stream().map((sysUserRole) -> sysUserRole.getRoleId()).collect(Collectors.toList());
        //创建一个返回的map
        Map<String, Object> returnMap = new HashMap<>();
        //将所有对象返回放到allRoles里
        returnMap.put("allRoles", sysRoles);
        //将用户已分配的角色信息的id值返回放到userRoleIds里
        returnMap.put("userRoleIds", collect);
        return returnMap;
    }

    @Override
    public void updateRoles(AssignRoleVo assignRoleVo) {
        //获取用户的id
        Long userId = assignRoleVo.getUserId();
        //清空已存在的角色信息
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //获取更新后的角色的id
        List<Long> roleIdList = assignRoleVo.getRoleIdList();
        //遍历每个角色的id
        if (roleIdList != null && roleIdList.size() > 0) {
            for (Long roleId : roleIdList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(roleId);
                sysUserRole.setUserId(userId);
                //创建对象将其插入表格
                sysUserRoleMapper.insert(sysUserRole);
            }
        }
    }
}
