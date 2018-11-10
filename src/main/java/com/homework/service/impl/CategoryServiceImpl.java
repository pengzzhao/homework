package com.homework.service.impl;

import com.homework.entity.Category;
import com.homework.mapper.CategoryMapper;
import com.homework.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lv-success
 * @since 2018-10-14
 */
@Service
public class CategoryServiceImpl extends BaseServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public void join(Map<String, Object> map, String field) {

        Map<String, Object> joinColumns = new HashMap<>();
        //字段的值
        String linkfieldValue = map.get(field).toString();
        Category category = this.getById(linkfieldValue);

        joinColumns.put("id", category.getId());
        joinColumns.put("name", category.getName());
        joinColumns.put("icon", category.getIcon());

        map.put("category", joinColumns);
    }
}
