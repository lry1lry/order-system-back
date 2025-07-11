package icu.lry.ordersystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Type {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer productId;
    private Integer bigTypeId;
}
