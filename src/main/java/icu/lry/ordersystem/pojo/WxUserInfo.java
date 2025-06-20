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
@TableName(value = "wxuserinfo")
public class WxUserInfo {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String openid;
    @TableField(value = "nickName")
    private String nickName;
    @TableField(value = "avatarUrl")
    private String avatarUrl;
    @TableField(value = "registerDate")
    private LocalDateTime registerDate;
    @TableField(value = "lastLoginDate")
    private LocalDateTime lastLoginDate;
}
