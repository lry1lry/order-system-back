package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.BigTypeMapperPlus;
import icu.lry.ordersystem.pojo.BigType;
import icu.lry.ordersystem.service.BigTypeServicePlus;
import org.springframework.stereotype.Service;

@Service
public class BigTypeServiceImplPlus extends ServiceImpl<BigTypeMapperPlus, BigType> implements BigTypeServicePlus{
}
