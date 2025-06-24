package icu.lry.ordersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import icu.lry.ordersystem.pojo.Cart;
import icu.lry.ordersystem.pojo.Product;
import icu.lry.ordersystem.service.CartServicePlus;
import icu.lry.ordersystem.service.ProductServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartServicePlus cartServicePlus;

    @Autowired
    private ProductServicePlus productServicePlus;

    @PostMapping("/insertCart")
    public Result insertCart(@RequestBody Cart cart) {
        LambdaQueryWrapper<Cart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cart::getOpenid, cart.getOpenid()).eq(Cart::getProductId, cart.getProductId());
        List<Cart> list = cartServicePlus.list(lqw);
        // 如果传过来的商品数量大于200，返回一个错误提示
        if(cart.getNum() > 200) {
            return Result.error("数量超过限制");
        }
        // 没找到该用户的相关购物车的信息，直接插入
        if(list.isEmpty()) {
            boolean flag = cartServicePlus.save(cart);
            if(flag) {
                return Result.success("加入购物车成功");
            }
            return Result.error("加入购物车失败");
        }
        // 如果购物车中商品原本的数量 + 现在传过来的商品数量 > 200，返回一个错误提示
        if(list.get(0).getNum() + cart.getNum() > 200) {
            return Result.error("数量超过限制");
        }
        // 更新现在购物车中商品的数量
        LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<>();
        luw.eq(Cart::getOpenid, cart.getOpenid()).eq(Cart::getProductId, cart.getProductId());
        luw.set(Cart::getNum, cart.getNum() + list.get(0).getNum());
        boolean flag = cartServicePlus.update(luw);
        if(flag) {
            return Result.success("加入购物车成功");
        }
        return Result.error("加入购物车失败");
    }

    @GetMapping("/getAllCart/{openid}")
    public Result getAllCart(@PathVariable String openid) {
        //查询该用户的所有购物车的信息
        LambdaQueryWrapper<Cart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cart::getOpenid, openid);
        lqw.orderByDesc(Cart::getId);
        List<Cart> cartList = cartServicePlus.list(lqw);
        //根据购物车的信息回去商品id,然后获取对应商品的数据
        ArrayList<Product> productList = new ArrayList<>();
        for (Cart cart : cartList) {
            LambdaQueryWrapper<Product> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Product::getId, cart.getProductId());
            Product p = productServicePlus.getOne(lqw1);
            productList.add(p);
        }
        return Result.success1(cartList, productList);
    }

    @PostMapping("/updateCartChecked")
    // 更新购物车选中状态
    public Result updateCartChecked(@RequestBody Cart cart) {
        LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<>();
        luw.set(Cart::getIsChecked, cart.getIsChecked());
        luw.eq(Cart::getOpenid, cart.getOpenid());
        luw.eq(Cart::getId, cart.getId());
        boolean flag = cartServicePlus.update(luw);
        if(flag) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @PostMapping("/updateCartNum")
    // 更新购物车数量
    public Result updateCartNum(@RequestBody Cart cart) {
        LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<>();
        luw.set(Cart::getNum, cart.getNum());
        luw.eq(Cart::getOpenid, cart.getOpenid());
        luw.eq(Cart::getId, cart.getId());
        boolean flag = cartServicePlus.update(luw);
        if(flag) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @GetMapping("/updateAllCart/{value}/{openid}")
    // 当用户全选反选时，更新该用户整个购物车的状态
    public Result updateAllCart(@PathVariable Integer value, @PathVariable String openid) {
        LambdaUpdateWrapper<Cart> luw = new LambdaUpdateWrapper<>();
        luw.set(Cart::getIsChecked, value);
        luw.eq(Cart::getOpenid, openid);
        boolean flag = cartServicePlus.update(luw);
        if(flag) {
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @GetMapping("/getAllCartByChecked/{openid}")
    public Result getAllCartByChecked(@PathVariable String openid) {
        //查询该用户的所有购物车的信息
        LambdaQueryWrapper<Cart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cart::getOpenid, openid);
        lqw.eq(Cart::getIsChecked, 1);
        lqw.orderByDesc(Cart::getId);
        List<Cart> cartList = cartServicePlus.list(lqw);
        //根据购物车的信息获取商品id,然后获取对应商品的数据
        ArrayList<Product> productList = new ArrayList<>();
        for (Cart cart : cartList) {
            LambdaQueryWrapper<Product> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Product::getId, cart.getProductId());
            Product p = productServicePlus.getOne(lqw1);
            productList.add(p);
        }
        return Result.success1(cartList, productList);
    }

    @GetMapping("/deleteCartByChecked/{openid}")
    public Result deleteCartByChecked(@PathVariable String openid) {
        // 去除该用户购物车中已选中的商品
        LambdaQueryWrapper<Cart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cart::getIsChecked, 1);
        lqw.eq(Cart::getOpenid, openid);
        cartServicePlus.remove(lqw);
        return Result.success();
    }

    @GetMapping("/deleteCartById/{id}")
    // 通过id删除购物车相关商品
    public Result deleteCartById(@PathVariable Integer id) {
        LambdaQueryWrapper<Cart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Cart::getId, id);
        cartServicePlus.remove(lqw);
        return Result.success();
    }
}
