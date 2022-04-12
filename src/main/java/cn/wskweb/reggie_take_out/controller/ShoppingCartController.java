package cn.wskweb.reggie_take_out.controller;


import cn.wskweb.reggie_take_out.common.BaseContext;
import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.entity.ShoppingCart;
import cn.wskweb.reggie_take_out.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        Long threadLocal = BaseContext.getThreadLocal();
        shoppingCart.setUserId(threadLocal);


        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 用户id
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, threadLocal);


        // 判断传入的是菜品，还是套餐，将其按id查询
        if (shoppingCart.getDishId() != null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCartOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        if (shoppingCartOne == null) {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartOne = shoppingCart;
        } else {
            Integer number = shoppingCartOne.getNumber();
            shoppingCartOne.setNumber(number + 1);
            shoppingCartService.updateById(shoppingCartOne);
        }

        return R.success(shoppingCartOne);
    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();

        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, BaseContext.getThreadLocal());
        shoppingCartLambdaQueryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(shoppingCartLambdaQueryWrapper);

        return R.success(list);
    }

    @PostMapping("sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        log.info(shoppingCart.toString());

        Long threadLocal = BaseContext.getThreadLocal();
        shoppingCart.setUserId(threadLocal);


        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();

        // 用户id
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, threadLocal);


        // 判断传入的是菜品，还是套餐，将其按id查询
        if (shoppingCart.getDishId() != null) {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCartOne = shoppingCartService.getOne(shoppingCartLambdaQueryWrapper);

        if (shoppingCartOne.getNumber() == 1) {
            shoppingCartService.removeById(shoppingCartOne);
            shoppingCartOne = shoppingCart;
        } else {
            Integer number = shoppingCartOne.getNumber();
            shoppingCartOne.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCartOne);
        }
        return R.success(shoppingCartOne);

    }

    @DeleteMapping("clean")
    public R<String> clean() {
        Long threadLocal = BaseContext.getThreadLocal();
        LambdaQueryWrapper<ShoppingCart> shoppingCartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        shoppingCartLambdaQueryWrapper.eq(ShoppingCart::getUserId, threadLocal);

        shoppingCartService.remove(shoppingCartLambdaQueryWrapper);

        return R.success("清空成功");

    }

}
