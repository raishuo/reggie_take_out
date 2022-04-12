package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.common.CustomException;
import cn.wskweb.reggie_take_out.entity.Category;
import cn.wskweb.reggie_take_out.entity.Dish;
import cn.wskweb.reggie_take_out.entity.Setmeal;
import cn.wskweb.reggie_take_out.mapper.CategoryMapper;
import cn.wskweb.reggie_take_out.service.CategoryService;
import cn.wskweb.reggie_take_out.service.DishService;
import cn.wskweb.reggie_take_out.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * @方法名: remove
     * @描述: 根据id删除分类，删除之前需要进行判断是否关联菜品或套餐
     * @param:
     * @return:
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件，按id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count = dishService.count(dishLambdaQueryWrapper);
        // 查询当前分类是否关联了菜品，如果已经关联，抛出业务异常
        if (count > 0) {
            throw new CustomException("当前关联了菜品无法删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count1 = setmealService.count(setmealLambdaQueryWrapper);
        // 查询当前分类是否关联了套餐，如果已经关联，抛出业务异常
        if (count1 > 0) {
            throw new CustomException("当前关联了套餐无法删除");
        }
        // 正常删除
        super.removeById(id);

    }
}
