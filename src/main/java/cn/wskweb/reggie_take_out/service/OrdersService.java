package cn.wskweb.reggie_take_out.service;

import cn.wskweb.reggie_take_out.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);
}
