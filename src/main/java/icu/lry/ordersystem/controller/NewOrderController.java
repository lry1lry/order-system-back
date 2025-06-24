package icu.lry.ordersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import icu.lry.ordersystem.pojo.Cart;
import icu.lry.ordersystem.pojo.NewOrder;
import icu.lry.ordersystem.pojo.Product;
import icu.lry.ordersystem.service.CartServicePlus;
import icu.lry.ordersystem.service.NewOrderServicePlus;
import icu.lry.ordersystem.service.ProductServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/newOrder")
@Slf4j
public class NewOrderController {

    @Autowired
    private NewOrderServicePlus newOrderServicePlus;

    @Autowired
    private CartServicePlus cartServicePlus;

    @Autowired
    private ProductServicePlus productServicePlus;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/insertNewOrder")
    public Result insertNewOrder(@RequestBody NewOrder newOrder) {
        // 创建订单编号
        LocalDateTime now = LocalDateTime.now();
        long time = System.currentTimeMillis();
        String orderNum = now.getYear() + newOrder.getOpenid() + time;
        // 获取商品id
        LambdaQueryWrapper<Cart> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Cart::getOpenid, newOrder.getOpenid());
        lqw1.eq(Cart::getIsChecked, 1);
        List<Cart> productIdList = cartServicePlus.list(lqw1);
        List<Cart> productNumList = new ArrayList<>();
        // 获取对应商品数量
        for (Cart cart : productIdList) {
            LambdaQueryWrapper<Cart> lqw2 = new LambdaQueryWrapper<>();
            lqw2.eq(Cart::getOpenid, newOrder.getOpenid());
            lqw2.eq(Cart::getProductId, cart.getProductId());
            List<Cart> productNumList1 = cartServicePlus.list(lqw2);
            productNumList.add(productNumList1.get(0));
        }
        // 将订单信息装入到集合中并插入到订单表中
        ArrayList<NewOrder> arr = new ArrayList<>();
        for (int i = 0; i < productIdList.size(); i++) {
            NewOrder order = new NewOrder(null, orderNum, newOrder.getOpenid(),
                    productIdList.get(i).getProductId(),
                    productNumList.get(i).getNum(),
                    newOrder.getAddress(), newOrder.getConsignee(),
                    now, null, "0");
            arr.add(order);
        }
        stringRedisTemplate.opsForHash().put("order", now.getYear() + " " + newOrder.getOpenid() + " " + time, "0");
        boolean flag = newOrderServicePlus.saveBatch(arr);
        if (flag) {
            return Result.success1("订单提交成功", orderNum);
        }
        return Result.error("订单提交失败");
    }

    @GetMapping("/getRemainTime/{orderNum}")
    // 获得距离提交订单到现在的时间
    public Result getRemainTime(@PathVariable String orderNum) {
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getOrderNum, orderNum);
        List<NewOrder> list = newOrderServicePlus.list(lqw);
        long oldTime = list.get(0).getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
        long newTime = System.currentTimeMillis();
        long time = newTime / 1000 - oldTime;
        return Result.success(time);
    }

    @PostMapping("/updateNewOrder")
    // 更新订单信息
    public Result updateNewOrder(@RequestBody NewOrder newOrder) {
        // 拿到相关订单信息
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getOrderNum, newOrder.getOrderNum());
        List<NewOrder> list = newOrderServicePlus.list(lqw);
        // 判断是否超时，如果已经超时，返回一个错误信息
        long oldTime = list.get(0).getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
        long newTime = System.currentTimeMillis();
        long time = newTime / 1000 - oldTime;
        if (time > 15 * 60) {
            return Result.success("订单超时");
        }
        LambdaUpdateWrapper<NewOrder> luw = new LambdaUpdateWrapper<>();
        luw.set(NewOrder::getStatus, newOrder.getStatus());
        //如果为已支付状态，需要修改支付时间，并且修改销量
        if(newOrder.getStatus().equals("1")) {
            // 设置时间
            luw.set(NewOrder::getPayTime, LocalDateTime.now());
            //获取所有订单信息
            LambdaQueryWrapper<NewOrder> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(NewOrder::getOrderNum, newOrder.getOrderNum());
            List<NewOrder> list1 = newOrderServicePlus.list(lqw1);
            //将相关订单的id和购买数量放到集合中
            ArrayList<Integer> arr = new ArrayList<>();
            ArrayList<Integer> arr1 = new ArrayList<>();
            for (NewOrder order : list1) {
                arr.add(order.getProductId());
                arr1.add(order.getProductNum());
            }
            //依次修改每个订单的销量
            for (int i = 0; i < arr.size(); i++) {
                LambdaQueryWrapper<Product> lqw2 = new LambdaQueryWrapper<>();
                lqw2.eq(Product::getId, arr.get(i));
                Product product = productServicePlus.getOne(lqw2);
                LambdaUpdateWrapper<Product> luw1 = new LambdaUpdateWrapper<>();
                luw1.eq(Product::getId, arr.get(i));
                luw1.set(Product::getSale, product.getSale() + arr1.get(i));
                productServicePlus.update(luw1);
            }
        }
        // 更新订单
        luw.eq(NewOrder::getOrderNum, newOrder.getOrderNum());
        boolean flag = newOrderServicePlus.update(luw);
        if (flag) {
            return Result.success("订单处理成功");
        }
        return Result.error("订单处理失败");
    }

    @GetMapping("/getIsCancelOrPayOrder/{orderNum}")
    // 获得订单是否已经支付或取消
    public Result getIsCancelOrder(@PathVariable String orderNum) {
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getOrderNum, orderNum);
        List<NewOrder> list = newOrderServicePlus.list(lqw);
        if (list.get(0).getStatus().equals("2")) {
            return Result.success("订单已取消");
        }
        if (list.get(0).getStatus().equals("1")) {
            return Result.success("订单已支付");
        }
        return Result.success("订单可以取消或支付");
    }

    @PostMapping("/cancelOrderAuto")
    // 系统自动取消订单
    public Result cancelOrderAuto(@RequestBody NewOrder newOrder) {
        LambdaUpdateWrapper<NewOrder> luw = new LambdaUpdateWrapper<>();
        luw.set(NewOrder::getStatus, newOrder.getStatus());
        luw.eq(NewOrder::getOrderNum, newOrder.getOrderNum());
        boolean flag = newOrderServicePlus.update(luw);
        if (flag) {
            return Result.success("订单取消成功");
        }
        return Result.error("订单取消失败");
    }

    @GetMapping("/checkTimeOutOrder")
    // 检查是否有订单超时
    public Result checkTimeOutOrder() {
        // 获取所有未支付的订单
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getStatus, "0");
        List<NewOrder> list = newOrderServicePlus.list(lqw);
        // 将未支付订单中超时的订单id存放到集合中
        long newTime = System.currentTimeMillis();
        ArrayList<Integer> arr = new ArrayList<>();
        for (NewOrder newOrder : list) {
            long oldTime = newOrder.getCreateTime().toEpochSecond(ZoneOffset.of("+8"));
            if ((newTime / 1000 - oldTime) > 15 * 60) {
                arr.add(newOrder.getId());
            }
        }
        // 如果集合为空，无需修改，提升性能
        if (arr.isEmpty()) {
            return Result.success();
        }
        // 进行批量修改
        LambdaUpdateWrapper<NewOrder> luw = new LambdaUpdateWrapper<>();
        luw.set(NewOrder::getStatus, "2");
        luw.in(NewOrder::getId, arr);
        newOrderServicePlus.update(luw);
        return Result.success();
    }

    @GetMapping("/getAllOrderByOpenidAndStatus/{openid}/{status}")
    public Result getAllOrderByOpenidAndStatus(@PathVariable String openid, @PathVariable String status) {
        // 获取该用户的相关订单信息
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getOpenid, openid);
        lqw.orderByDesc(NewOrder::getId);
        if (!status.equals("3")) {
            lqw.eq(NewOrder::getStatus, status);
        }
        List<NewOrder> list = newOrderServicePlus.list(lqw);
        // 根据订单的商品id找到对应商品的信息
        ArrayList<Product> arr = new ArrayList<>();
        for (NewOrder newOrder : list) {
            LambdaQueryWrapper<Product> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Product::getId, newOrder.getProductId());
            Product product = productServicePlus.getOne(lqw1);
            arr.add(product);
        }
        return Result.success1(list, arr);
    }

    @GetMapping("/getOneOrderById/{id}")
    // 获取指定订单的相关信息
    public Result getOneOrderById(@PathVariable Integer id) {
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getId, id);
        NewOrder order = newOrderServicePlus.getOne(lqw);
        // 根据商品id找到对应商品信息
        LambdaQueryWrapper<Product> lqw1 = new LambdaQueryWrapper<>();
        lqw1.eq(Product::getId, order.getProductId());
        Product product = productServicePlus.getOne(lqw1);
        return Result.success1(order, product);
    }

    @GetMapping("/getOrderByOrderNum/{orderNum}")
    public Result getOrderByOrderNum(@PathVariable String orderNum) {
        LambdaQueryWrapper<NewOrder> lqw = new LambdaQueryWrapper<>();
        lqw.eq(NewOrder::getOrderNum, orderNum);
        List<NewOrder> list = newOrderServicePlus.list(lqw);
        // 根据订单的商品id找到对应商品的信息
        ArrayList<Product> arr = new ArrayList<>();
        for (NewOrder newOrder : list) {
            LambdaQueryWrapper<Product> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Product::getId, newOrder.getProductId());
            Product product = productServicePlus.getOne(lqw1);
            arr.add(product);
        }
        return Result.success1(list, arr);
    }

    @PostMapping("/insertOneNewOrder")
    public Result insertOneNewOrder(@RequestBody NewOrder newOrder) {
        // 创建订单编号
        LocalDateTime now = LocalDateTime.now();
        long time = System.currentTimeMillis();
        String orderNum = now.getYear() + newOrder.getOpenid() + time;
        // 获取商品id
        Integer productId = newOrder.getProductId();
        // 获取对应商品数量
        Integer productNum = newOrder.getProductNum();
        // 将订单信息装入到集合中并插入到订单表中
        NewOrder order = new NewOrder(null, orderNum, newOrder.getOpenid(),
                productId, productNum, newOrder.getAddress(), newOrder.getConsignee(),
                now, null, "0");
        stringRedisTemplate.opsForHash().put("order", now.getYear() + " " + newOrder.getOpenid() + " " + time, "0");
        boolean flag = newOrderServicePlus.save(order);
        if (flag) {
            return Result.success1("订单提交成功", orderNum);
        }
        return Result.error("订单提交失败");
    }
}
