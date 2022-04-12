package cn.wskweb.reggie_take_out.service;

import cn.wskweb.reggie_take_out.dto.SetmealDto;
import cn.wskweb.reggie_take_out.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    SetmealDto getByWithDish(Long id);

    void  updateWithDish(SetmealDto setmealDto);
}
