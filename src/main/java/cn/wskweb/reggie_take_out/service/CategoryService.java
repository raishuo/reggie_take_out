package cn.wskweb.reggie_take_out.service;

import cn.wskweb.reggie_take_out.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
