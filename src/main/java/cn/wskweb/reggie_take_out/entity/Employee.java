package cn.wskweb.reggie_take_out.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String name;
    private String password;
    private String phone;
    private String sex;

    // 在数据库中字段为id_number 需要在配置文件中开启驼峰命名法
    // map-underscore-to-camel-case: true
    // 在映射实体或者属性时，将数据库中表名和字段名中的下划线去掉，按照驼峰命名法映射
    private String idNumber;
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /*
     * FieldFill.DEFAULT:字段自动填充策略
     * DEFAULT	默认不处理
     * INSERT	插入时填充字段
     * UPDATE	更新时填充字段
     * INSERT_UPDATE	插入和更新时填充字段
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
