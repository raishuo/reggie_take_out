package cn.wskweb.reggie_take_out.dto;

import cn.wskweb.reggie_take_out.entity.Setmeal;
import cn.wskweb.reggie_take_out.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
