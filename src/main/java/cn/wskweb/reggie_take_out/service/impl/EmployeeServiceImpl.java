package cn.wskweb.reggie_take_out.service.impl;

import cn.wskweb.reggie_take_out.entity.Employee;
import cn.wskweb.reggie_take_out.mapper.EmployeeMapper;
import cn.wskweb.reggie_take_out.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
