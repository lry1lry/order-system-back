package icu.lry.ordersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.lry.ordersystem.pojo.BigType;
import icu.lry.ordersystem.pojo.Product;
import icu.lry.ordersystem.pojo.Type;
import icu.lry.ordersystem.service.BigTypeServicePlus;
import icu.lry.ordersystem.service.ProductServicePlus;
import icu.lry.ordersystem.service.TypeServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private TypeServicePlus typeServicePlus;

    @Autowired
    private ProductServicePlus productServicePlus;

    @Autowired
    private BigTypeServicePlus bigTypeServicePlus;

    @GetMapping("/getAllType")
    // 得到所有分类
    public Result getAllType() {
        List<Type> typeList = typeServicePlus.list();
        ArrayList<Product> arr = new ArrayList<>();
        //通过商品id找到对应商品
        for (Type type : typeList) {
            LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Product::getId, type.getProductId());
            Product product = productServicePlus.getOne(lqw);
            arr.add(product);
        }
        //通过商品大类id找到对应商品大类
        ArrayList<BigType> arr1 = new ArrayList<>();
        for (Type type : typeList) {
            LambdaQueryWrapper<BigType> lqw = new LambdaQueryWrapper<>();
            lqw.eq(BigType::getId, type.getBigTypeId());
            BigType bigType = bigTypeServicePlus.getOne(lqw);
            arr1.add(bigType);
        }
        return Result.success1(arr, arr1);
    }
}
