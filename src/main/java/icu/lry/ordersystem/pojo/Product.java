package icu.lry.ordersystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Double price;
    private Integer stock;
    @TableField(value = "proPic")
    private String proPic = "default.jpg";
    @TableField(value = "isHot")
    private Boolean isHot = false;
    @TableField(value = "isSwiper")
    private Boolean isSwiper = false;
    @TableField(value = "swiperPic")
    private String swiperPic = "default.jpg";
    @TableField(value = "swiperSort")
    private Integer swiperSort = 0;
    @TableField(value = "typeId")
    private Integer typeId;
    @TableField(value = "hotDateTime")
    private LocalDateTime hotDateTime;
    @TableField(value = "productIntroImgs")
    private String productIntroImgs;
    @TableField(value = "productParaImgs")
    private String productParaImgs;
    private String description;
    private Integer sale;
}
