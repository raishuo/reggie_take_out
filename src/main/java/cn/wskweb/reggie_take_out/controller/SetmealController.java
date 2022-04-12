package cn.wskweb.reggie_take_out.controller;

import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.dto.DishDto;
import cn.wskweb.reggie_take_out.dto.SetmealDto;
import cn.wskweb.reggie_take_out.entity.Category;
import cn.wskweb.reggie_take_out.entity.Dish;
import cn.wskweb.reggie_take_out.entity.Setmeal;
import cn.wskweb.reggie_take_out.service.CategoryService;
import cn.wskweb.reggie_take_out.service.SetmealDishService;
import cn.wskweb.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @创建人: wsk
 * @描述: 套餐管理
 * @className（类名称）: SetmealController
 * @创建时间: 18:18 2022/4/7
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * @方法名:  save
     * @描述: 添加套餐
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("成功");
    }


    /**
     * @方法名:  page
     * @描述: 分页查询套餐
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }



    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return R.success("删除成功");
    }



    @GetMapping("/{id}")
    public R<SetmealDto> update(@PathVariable Long id) {
        return R.success(setmealService.getByWithDish(id));
    }

    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        // 删除后添加 == 修改  ps 我真他喵的机智
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }

    @PostMapping("/status/{id}")
    public R<String> status(@PathVariable Integer id, @RequestParam List<Long> ids) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(id);
        setmealService.update(setmeal, queryWrapper);

        return R.success("成功");
    }
    
    /**
     * @方法名:  lsit
     * @描述: 根据条件查询套餐 
     * @param:  
     * @return:  
     */
    @GetMapping("/list")
    public R<List<Setmeal>> lsit(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        setmealLambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        setmealLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(setmealLambdaQueryWrapper);
        return R.success(list);
    }

}
