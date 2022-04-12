package cn.wskweb.reggie_take_out.controller;

import cn.wskweb.reggie_take_out.common.R;
import cn.wskweb.reggie_take_out.entity.Employee;
import cn.wskweb.reggie_take_out.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @创建人: wsk
 * @描述: 访问路径employee，Controller（控制器）
 * @创建时间: 18:37 2022/4/1
 * @返回值 null
 */
@Slf4j
@RestController
@RequestMapping("employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    /**
     * @方法名: login
     * @描述: 员工登录
     * @返回值: R.success(emp)
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        // 将提交的密码进行md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());     // getBytes() : 使用指定的字符集将字符串编码为 byte 序列，并将结果存储到一个新的 byte 数组中。

        // 根据用户提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> objectLambdaQueryWrapper = new LambdaQueryWrapper<>(); // 导入Lambda查询包装器
        objectLambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());     // mybatisPlus中equals() , 作用是查询数据库中是否有post请求提交的username

        Employee emp = employeeService.getOne(objectLambdaQueryWrapper);    // 根据 objectLambdaQueryWrapper,查询一条记录  数据库中username为唯一约束

        // 查询结果为null（无此用户名）时直接返回 ： 登录失败
        if (emp == null) {
            return R.error("登录失败");
        }

        // 密码比对，不同时返回 ： 登录失败
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        // 查看用户的状态，如果为禁用状态， 返回 ： 账号已经禁用
        if (emp.getStatus() == 0) {
            return R.error("账号已经禁用");
        }

        // 登录成功后，将员工id存入Session并返回登录成功结果集
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);

    }

    /**
     * @方法名: logout
     * @描述: 员工退出
     * @返回值: R.success(" 退出成功 ")
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        // 清理当前Session中当前员工的id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功");
    }

    /**
     * @方法名: save
     * @描述: 新增员工
     * @param(传入参数): employee
     * @return: String
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee) {
        log.info("新增员工信息:{}", employee.toString());

        // 设置初始密码，MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 存入数据库
        employeeService.save(employee);
        return R.success("新增员工成功");
    }


    /**
     * @方法名: page
     * @描述:
     * @param(传入参数): page 分页第几页
     * @param(传入参数): pageSize 分几页
     * @param(传入参数): name  按name查询
     * @return:
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造分页构造器
        Page<Employee> PageInfo = new Page<>(page, pageSize);
        // 构造条件构造器
        LambdaQueryWrapper<Employee> objectLambdaQueryWrapper = new LambdaQueryWrapper<>();
        objectLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);      // 添加过滤条件
        objectLambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);      // 添加排序条件
        employeeService.page(PageInfo, objectLambdaQueryWrapper);// 执行查询
        return R.success(PageInfo);

    }


    /**
     * @方法名: update
     * @描述: 根据id修改员工信息
     * @param(传入参数):
     * @return:
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        employeeService.updateById(employee);

        return R.success("成功");
    }


    /**
     * @方法名: getById
     * @描述: 按id查询信息并返回给网页
     * @param: id 从网页传来的数据
     * @return:
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee byId = employeeService.getById(id);

        return R.success(byId);
    }

}
