package cn.wskweb.reggie_take_out.dto;

import cn.wskweb.reggie_take_out.entity.Dish;
import cn.wskweb.reggie_take_out.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
