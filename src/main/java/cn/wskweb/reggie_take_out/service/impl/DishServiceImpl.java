package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.dto.DishDto;
import cn.wskweb.reggie_take_out.entity.Dish;
import cn.wskweb.reggie_take_out.entity.DishFlavor;
import cn.wskweb.reggie_take_out.mapper.DishMapper;
import cn.wskweb.reggie_take_out.service.DishFlavorService;
import cn.wskweb.reggie_take_out.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;


    /**
     * @方法名:  saveWithFlavor
     * @描述: 新增菜品同时添加口味数据
     * @param:
     */
    @Override
    @Transactional      // 声明式事务管理
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto); // 保存基本信息到dish


        Long disId = dishDto.getId();       // 菜品id

        // 菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(disId);
            return item;
        }).collect(Collectors.toList());

        // 保存菜品到口味表
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByWithFlavor(Long id) {
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish, dishDto);    // 拷贝


        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());

        List<DishFlavor> list = dishFlavorService.list(dishFlavorLambdaQueryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        // 清理当前菜品的口味数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        // 添加当前口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();


        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }


}
