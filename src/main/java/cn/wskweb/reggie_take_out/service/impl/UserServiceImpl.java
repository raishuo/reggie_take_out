package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.entity.User;
import cn.wskweb.reggie_take_out.mapper.UserMapper;
import cn.wskweb.reggie_take_out.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
