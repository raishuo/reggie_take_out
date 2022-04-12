package cn.wskweb.reggie_take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/**
 * @创建人: wsk
 * @描述: 自定义元数据处理器
 * @className（类名称）: MyMetaObjecthandler
 * @创建时间: 17:39 2022/4/3
 */

@Slf4j
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {

    /**
     * @方法名: insertFill
     * @描述: 插入操作自动填充，当执行插入操作时自动填充以下字段
     * @param:
     * @return:
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //                            字段               值
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getThreadLocal());
        metaObject.setValue("createUser", BaseContext.getThreadLocal());
    }


    /**
     * @方法名: insertFill
     * @描述: 跟新操作自动填充
     * @param:
     * @return:
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getThreadLocal());
    }
}
