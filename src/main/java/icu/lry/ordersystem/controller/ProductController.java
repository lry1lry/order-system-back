package icu.lry.ordersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.lry.ordersystem.pojo.Product;
import icu.lry.ordersystem.service.ProductServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductServicePlus productServicePlus;

    @GetMapping("/getProductLbtPic")
    public Result getProductLbtPic() {
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Product::getIsSwiper, true);
        List<Product> picList = productServicePlus.list(lqw);
        return Result.success(picList);
    }

    @GetMapping("/getHotProduct")
    public Result getHotProduct() {
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Product::getIsHot, true);
        List<Product> list = productServicePlus.list(lqw);
        return Result.success(list);
    }

    @GetMapping("/getSearchProduct/{value}")
    public Result getSearchProduct(@PathVariable String value) {
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.like(Product::getName, value);
        List<Product> list = productServicePlus.list(lqw);
        return Result.success(list);
    }

    @GetMapping("/getOneProductById/{id}")
    public Result getOneProductById(@PathVariable Integer id) {
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Product::getId, id);
        Product product = productServicePlus.getOne(lqw);
        return Result.success(product);
    }

    @GetMapping("/getSearchProductByList/{ids}")
    public Result getSearchProductByList(@PathVariable String[] ids) {
        List<String> ids1 = List.of(ids);
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.in(Product::getId, ids1);
        List<Product> list = productServicePlus.list(lqw);
        return Result.success(list);
    }

    @GetMapping("/getSearchProductByListOrderBySale/{ids}")
    public Result getSearchProductByListOrderBySale(@PathVariable String[] ids) {
        List<String> ids1 = List.of(ids);
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.in(Product::getId, ids1);
        lqw.orderByDesc(Product::getSale);
        List<Product> list = productServicePlus.list(lqw);
        return Result.success(list);
    }

    @GetMapping("/getSearchProductByListOrderByPriceAsc/{ids}")
    public Result getSearchProductByListOrderByPriceAsc(@PathVariable String[] ids) {
        List<String> ids1 = List.of(ids);
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.in(Product::getId, ids1);
        lqw.orderByAsc(Product::getPrice);
        List<Product> list = productServicePlus.list(lqw);
        return Result.success(list);
    }

    @GetMapping("/getSearchProductByListOrderByPriceDesc/{ids}")
    public Result getSearchProductByListOrderByPriceDesc(@PathVariable String[] ids) {
        List<String> ids1 = List.of(ids);
        LambdaQueryWrapper<Product> lqw = new LambdaQueryWrapper<>();
        lqw.in(Product::getId, ids1);
        lqw.orderByDesc(Product::getPrice);
        List<Product> list = productServicePlus.list(lqw);
        return Result.success(list);
    }

}
