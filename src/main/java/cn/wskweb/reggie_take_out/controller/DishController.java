package cn.wskweb.reggie_take_out.controller;


import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.dto.DishDto;
import cn.wskweb.reggie_take_out.entity.Category;
import cn.wskweb.reggie_take_out.entity.Dish;
import cn.wskweb.reggie_take_out.entity.DishFlavor;
import cn.wskweb.reggie_take_out.entity.Setmeal;
import cn.wskweb.reggie_take_out.service.CategoryService;
import cn.wskweb.reggie_take_out.service.DishFlavorService;
import cn.wskweb.reggie_take_out.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @创建人: wsk
 * @描述: 菜品管理
 * @className（类名称）: DishController
 * @创建时间: 12:56 2022/4/5
 */

@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造对象器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();


        // 添加过滤条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);      // 添加过滤条件
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort);

        // 执行分页查询
        dishService.page(pageInfo, dishLambdaQueryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();

        // 菜品口味
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId(); // 分类id
            Category category = categoryService.getById(categoryId);
            // 根据id查询分类
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    /**
     * @方法名: save
     * @描述: 菜品管理，添加菜品
     * @param: dishDto : dto数据传输对象
     * @return:
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> update(@PathVariable Long id) {
        return R.success(dishService.getByWithFlavor(id));
    }


    /**
     * @方法名: update
     * @描述: 修改菜品
     * @param: dishDto : dto数据传输对象
     * @return:
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * @方法名: save
     * @描述: 菜品管理，删除菜品
     * @param: dishDto : dto数据传输对象
     * @return:
     */
    @DeleteMapping
    public R<String> delete(String ids) {
        List<String> idsList = Arrays.asList(ids.split(","));
        dishService.removeBatchByIds(idsList);
        return R.success("删除成功");
    }


    /**
     * @方法名: status
     * @描述: 菜品套餐停售起售    ######## ps: 这段代码写的是真的很很很垃圾  ---- wsk
     * @param:
     * @return:
     */
    @PostMapping("/status/{id}")
    public R<String> status(@PathVariable Integer id, @RequestParam List<Long> ids) {

        // ps: 这一段代码确实就是一坨屎  总结  SQL理解不到位
//        for (Long a : ids) {
//            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Dish::getId, a);
//            Dish dish = new Dish();
//            dish.setStatus(id);
//            dishService.update(dish, queryWrapper);
//        }

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        Dish dish = new Dish();
        dish.setStatus(id);
        dishService.update(dish, queryWrapper);

        return R.success("成功");
    }


    /**
     * @方法名: list
     * @描述: 根据条件查询对应菜品数据
     * @param: dish
     * @return:
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus, 1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> DishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId(); // 分类id
            Category category = categoryService.getById(categoryId);
            // 根据id查询分类
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            // 当前菜品的id
            Long dishId = item.getId();

            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());


        return R.success(DishDtoList);
    }


}
