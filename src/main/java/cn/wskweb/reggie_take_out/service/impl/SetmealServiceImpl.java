package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.common.CustomException;
import cn.wskweb.reggie_take_out.dto.SetmealDto;
import cn.wskweb.reggie_take_out.entity.Setmeal;
import cn.wskweb.reggie_take_out.entity.SetmealDish;
import cn.wskweb.reggie_take_out.mapper.SetmealMapper;
import cn.wskweb.reggie_take_out.service.SetmealDishService;
import cn.wskweb.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * @方法名:  saveWithDish
     * @描述: 新增套餐，陶氏需要保存套餐和菜品的关联关系
     * @param:
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * @方法名:  removeWithDish
     * @描述:  删除套餐，同时需要删除套餐和菜品的关联数据
     */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = Math.toIntExact(this.count(queryWrapper));
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
    }




    @Override
    @Transactional
    public SetmealDto getByWithDish(Long id) {
        Setmeal setmeal = this.getById(id);

        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);


        LambdaQueryWrapper<SetmealDish> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());

        List<SetmealDish> list = setmealDishService.list(setmealLambdaQueryWrapper);

        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        // 删除
        Long id = setmealDto.getId();
        //如果可以删除，先删除套餐表中的数据---setmeal
        this.removeById(id);
        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, id);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);

        saveWithDish(setmealDto);


    }


}
