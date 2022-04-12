package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.entity.DishFlavor;
import cn.wskweb.reggie_take_out.mapper.DishFlavorMapper;
import cn.wskweb.reggie_take_out.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
