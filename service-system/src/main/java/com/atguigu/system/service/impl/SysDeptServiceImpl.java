package com.atguigu.system.service.impl;

import com.atguigu.common.helper.DeptHelper;
import com.atguigu.model.system.SysDept;
import com.atguigu.system.exception.GuiguException;
import com.atguigu.system.mapper.SysDeptMapper;
import com.atguigu.system.service.SysDeptService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {


    @Override
    public List<SysDept> getDepts() {
        List<SysDept> sysDepts = baseMapper.selectList(new QueryWrapper<SysDept>().eq("is_deleted", 0));
        List<SysDept> depts = DeptHelper.buildTree(sysDepts, 0L);
        return depts;
    }

    @Override
    public boolean deleteById(Long id) {
        List<SysDept> sysDepts = baseMapper.selectList(new QueryWrapper<SysDept>().eq("status", 1).eq("is_deleted", 0).eq("parent_id", id));
        if (sysDepts.size() > 0) {
            throw new GuiguException(114, "无法删除该项节点");
        }
        int deleteById = baseMapper.deleteById(id);
        return true;
    }

    @Override
    public Integer updateButtonStatus(Long id, Integer status) {
        List<SysDept> sysDepts = baseMapper.selectList(new QueryWrapper<SysDept>().eq("is_deleted", 0).eq("parent_id", id));
        if (sysDepts.size() > 0) {
            for (SysDept sysDept : sysDepts) {
                SysDept dept = new SysDept();
                dept.setId(sysDept.getId());
                dept.setStatus(status);
                baseMapper.updateById(dept);
            }
        }
        SysDept sysDept = new SysDept();
        sysDept.setId(id);
        sysDept.setStatus(status);
        Integer updateById = baseMapper.updateById(sysDept);
        return updateById;
    }


}
