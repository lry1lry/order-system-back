package icu.lry.ordersystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "newOrder")
public class NewOrder {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField(value = "orderNum")
    private String orderNum;
    private String openid;
    private Integer productId;
    private Integer productNum;
    private String address;
    private String consignee;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private String status;
}
