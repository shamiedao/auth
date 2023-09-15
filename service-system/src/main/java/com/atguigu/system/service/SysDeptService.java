package com.atguigu.system.service;

import com.atguigu.model.system.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    List<SysDept> getDepts();

    boolean deleteById(Long id);

    Integer updateButtonStatus(Long id, Integer status);
}
