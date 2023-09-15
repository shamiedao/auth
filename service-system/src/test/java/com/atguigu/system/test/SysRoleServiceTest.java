package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SysRoleServiceTest {

    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    //测试查询所有
    @Test
    public void test() {
        List<SysRole> list = sysRoleService.list();
        list.forEach(System.out::println);
    }


    //
    @Test
    public void test01(){
        SysRole sysRole = new SysRole();
        sysRole.setId(12L);
        sysRole.setRoleCode("DEVS");
        int i = sysRoleMapper.updateById(sysRole);
        System.out.println(i);
    }
}
