package icu.lry.ordersystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import icu.lry.ordersystem.pojo.Address;
import icu.lry.ordersystem.service.AddressServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.analysis.function.Add;
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
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getUsername, address.getUsername())
                .eq(Address::getOpenid, address.getOpenid())
                .eq(Address::getCityName, address.getCityName())
                .eq(Address::getCountryName, address.getCountryName())
                .eq(Address::getDetailName, address.getDetailName())
                .eq(Address::getProvinceName, address.getProvinceName());
        List<Address> list = addressServicePlus.list(lqw);
        if(!list.isEmpty()) {
            return Result.error("当前地址已存在");
        }
        boolean flag = addressServicePlus.save(address);
        if (flag) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @GetMapping("/getDefaultAddress/{openid}")
    public Result getDefaultAddress(@PathVariable String openid) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getOpenid, openid).eq(Address::getIsDefault, 1);
        Address address = addressServicePlus.getOne(lqw);
        return Result.success(address);
    }
}
