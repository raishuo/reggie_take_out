package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.entity.ShoppingCart;
import cn.wskweb.reggie_take_out.mapper.ShoppingCartMapper;
import cn.wskweb.reggie_take_out.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
