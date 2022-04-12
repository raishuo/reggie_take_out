package cn.wskweb.reggie_take_out.controller;


import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.entity.Category;
import cn.wskweb.reggie_take_out.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @创建人: wsk
 * @描述: 网页分类管理功能
 * @className（类名称）: CategoryController
 * @创建时间: 15:33 2022/4/3
 * @返回值: null
 */
@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * @方法名: page
     * @描述: 分页查询
     * @return: R.success(PageInfo)
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 构造分页构造器
        Page<Category> PageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Category> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.orderByAsc(Category::getSort);      // 添加排序条件 orderByAsc : orderByAsc("id", "name")--->order by id ASC,name ASC
        categoryService.page(PageInfo, objectLambdaQueryWrapper);   // 执行查询

        return R.success(PageInfo);
    }


    /**
     * @方法名: save
     * @描述: 新增菜品信息
     * @param: Category
     * @return: R.success(String)
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("新增菜品信息:{}", category.toString());
        categoryService.save(category);
        return R.success("添加成功");
    }


    /**
     * @方法名: update
     * @描述: 根据id菜品信息
     * @param(传入参数):
     * @return:
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("成功");
    }


    @DeleteMapping
    public R<String> delete(Long id) {
        log.info("删除分类id为：{}", id);
        categoryService.remove(id);
        return R.success("成功");
    }


    /**
     * @方法名: lsit
     * @描述: 添加彩屏中菜品分类数据
     * @param: type 区分是菜品还是套餐
     * @return:
     */
    @GetMapping("/list")
    public R<List<Category>> lsit(Category category) {
        LambdaQueryWrapper<Category> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        objectLambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);      // 添加排序条件 orderByAsc : orderByAsc("id", "name")--->order by id ASC,name ASC
        List<Category> list = categoryService.list(objectLambdaQueryWrapper);
        return R.success(list);

    }
}
