package icu.lry.ordersystem.controller;

import icu.lry.ordersystem.pojo.BigType;
import icu.lry.ordersystem.service.BigTypeServicePlus;
import icu.lry.ordersystem.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/bigtype")
public class BigTypeController {

    @Autowired
    private BigTypeServicePlus bigTypeServicePlus;

    @GetMapping("/getBigType")
    // 获得商品大类的全部数据
    public Result getBigType() {
        List<BigType> list = bigTypeServicePlus.list();
        return Result.success(list);
    }
}
