package icu.lry.ordersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import icu.lry.ordersystem.pojo.Address;
import icu.lry.ordersystem.service.AddressServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/address")
@Slf4j
public class AddressController {

    @Autowired
    private AddressServicePlus addressServicePlus;

    @PostMapping("/insertAddress")
    public Result insertAddress(@RequestBody Address address) {
        //将该用户的所有地址都变成不默认
        LambdaUpdateWrapper<Address> luw = new LambdaUpdateWrapper<>();
        luw.set(Address::getIsDefault, 0);
        luw.eq(Address::getOpenid, address.getOpenid());
        addressServicePlus.update(luw);
        //查找是否有一样的地址
        LambdaQueryWrapper<Address> lqw0 = new LambdaQueryWrapper<>();
        lqw0.eq(Address::getUsername, address.getUsername())
                .eq(Address::getOpenid, address.getOpenid())
                .eq(Address::getCityName, address.getCityName())
                .eq(Address::getCountryName, address.getCountryName())
                .eq(Address::getDetailName, address.getDetailName())
                .eq(Address::getProvinceName, address.getProvinceName());
        List<Address> list = addressServicePlus.list(lqw0);
        //如果有一样的地址，将该地址变为默认
        if(!list.isEmpty()) {
            LambdaUpdateWrapper<Address> luw1 = new LambdaUpdateWrapper<>();
            luw1.set(Address::getIsDefault, 1);
            luw1.eq(Address::getId, list.get(0).getId());
            boolean flag = addressServicePlus.update(luw1);
            if (flag) {
                return Result.success("添加成功");
            }
            return Result.error("添加失败");
        }
        //如果没有该地址，插入该地址
        boolean flag = addressServicePlus.save(address);
        if (flag) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @GetMapping("/getDefaultAddress/{openid}")
    // 获得该用户的默认地址
    public Result getDefaultAddress(@PathVariable String openid) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getOpenid, openid).eq(Address::getIsDefault, 1);
        Address address = addressServicePlus.getOne(lqw);
        return Result.success(address);
    }
}
