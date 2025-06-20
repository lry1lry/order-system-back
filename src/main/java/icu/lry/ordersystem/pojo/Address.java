package icu.lry.ordersystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String openid;
    private String cityName;
    private String countryName;
    private String detailName;
    private String provinceName;
    private Integer isDefault = 1;
}
