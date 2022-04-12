package cn.wskweb.reggie_take_out.mapper;

import cn.wskweb.reggie_take_out.entity.Orders;
import cn.wskweb.reggie_take_out.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
