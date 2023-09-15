package com.atguigu.system.mapper;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends BaseMapper<SysUser> {
    Page<SysUser> getPages(Page<SysUser> sysUserPage,@Param("con") SysUserQueryVo sysUserQueryVo);

    boolean updateStatus(@Param("id") Long id,@Param("status") Integer status);
}
