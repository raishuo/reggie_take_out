package cn.wskweb.reggie_take_out.service;

import cn.wskweb.reggie_take_out.dto.DishDto;
import cn.wskweb.reggie_take_out.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    // 新增菜品，同时插入菜品相对应的口味数据，需要操作两张表dish、dish_flavor
    void saveWithFlavor(DishDto dishDto);

    // 根据id查询菜品信息和口味信息
    DishDto getByWithFlavor(Long id);

    // 更新数据
    void updateWithFlavor(DishDto dishDto);


}
