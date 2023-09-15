package com.atguigu.system.test;

import com.atguigu.model.system.SysRole;
import com.atguigu.system.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class SysRoleMapperTest {

    //    @Autowired
    @Resource
    private SysRoleMapper sysRoleMapper;

    //测试查询所有
    @Test
    public void test() {
        //UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
        //所以不填写就是无任何条件
        List<SysRole> users = sysRoleMapper.selectList(null);
        users.forEach(System.out::println);
    }

    //测试添加
    @Test
    public void testInsert() {
        //创建SysRole对象
        SysRole sysRole = new SysRole("西门庆", "dgr", "海王");
        //调用插入的方法
        int insert = sysRoleMapper.insert(sysRole);
        Long id = sysRole.getId();
        //获取插入对象的信息
        System.out.println("id = " + id);
        System.out.println(insert > 0 ? "插入成功" : "插入失败");
    }

    //测试删除
    @Test
    public void testdelete() {
        sysRoleMapper.deleteById(9);
    }

    //查询一个
    @Test
    public void testselectone() {
        SysRole sysRole = sysRoleMapper.selectById(8);
        System.out.println(sysRole);
    }

    //更新信息
    @Test
    public void testupdate() {
        SysRole sysRole = new SysRole();
        sysRole.setId(8L);
        sysRole.setDescription("用户管理员");
        sysRoleMapper.updateById(sysRole);
    }

    //测试有条件的查询
    @Test
    public void testConditionalSelect() {
        //创建QueryWrapper对象
//        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        UpdateWrapper<SysRole> updateWrapper = new UpdateWrapper<>();
        updateWrapper.ge(true, "role_code", "yhgly");
        //封装查询条件
        //todo 查询角色名为yhgly、描述为用户管理员的信息
//        queryWrapper.eq("role_code", "yhgly").ge("description","用户管理员");
        //todo 查询角色编码为COMMON或者描述为用户管理员的信息
//        queryWrapper.eq("role_code","COMMON").or().eq("description","用户管理员");
        //todo 查询id值大于8的用户信息
//        queryWrapper.ge("id",8);
        //todo 查询包含管理三字的用户
//        queryWrapper.like("role_name","管理");
        //todo 查询以系统开头的用户
//        queryWrapper.likeRight("role_name","系统");
        //todo 查询以庆结尾的用户
//        queryWrapper.likeLeft("role_name","庆");
//        queryWrapper.likeLeft("role_name","__管理员");
        //todo 指定查询的字段
//        queryWrapper.select("id","role_name","role_code");
        //调用SysRoleMapper中查询所有的方法
//        List<SysRole> sysRoles = sysRoleMapper.selectList(queryWrapper);
        sysRoleMapper.update(new SysRole("用户管理", "USER", "管理员"), updateWrapper);
//        sysRoles.forEach(System.out::println);
    }

    //测试使用LambdaQueryWrapper测试待条件的查询
    @Test
    public void testConditionalLambdaSelect() {
        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysRole::getRoleCode, "USER");
        List<SysRole> sysRoles = sysRoleMapper.selectList(lambdaQueryWrapper);
        sysRoles.forEach(System.out::println);
    }

    //测试分页
    @Test
    public void testPageHelper() {
        //创建page对象
        Page<SysRole> sysRolePage = new Page<>(2, 2);
        //调用SysRoleMapper中分页的方法
        Page<SysRole> rolePage = sysRoleMapper.selectPage(sysRolePage, null);
        //获取当前页
        System.out.println("当前页 = " + rolePage.getCurrent());
        //获取每页显示的条数
        System.out.println("每页显示的条数 = " + rolePage.getSize());
        //获取总条数
        System.out.println("总条数 = " + rolePage.getTotal());
        //获取总页数
        System.out.println("rolePage.getPages() = " + rolePage.getPages());
        //获取当前页的数据
        System.out.println("当前页的数据是：");
        List<SysRole> records = rolePage.getRecords();
        records.forEach(System.out::println);
    }
}
