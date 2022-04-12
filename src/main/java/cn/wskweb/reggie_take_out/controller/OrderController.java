package cn.wskweb.reggie_take_out.controller;


import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.entity.Employee;
import cn.wskweb.reggie_take_out.entity.Orders;
import cn.wskweb.reggie_take_out.service.OrdersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
            ordersService.submit(orders);
            return R.success("成功");
    }

    @GetMapping("userPage")
    public R<Page> page(int page, int pageSize) {
        // 构造分页构造器
        Page<Orders> PageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Orders> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);      // 添加排序条件
        ordersService.page(PageInfo, objectLambdaQueryWrapper);// 执行查询
        return R.success(PageInfo);
    }
}
