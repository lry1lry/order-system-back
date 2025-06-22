package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.NewOrderMapperPlus;
import icu.lry.ordersystem.pojo.NewOrder;
import icu.lry.ordersystem.service.NewOrderServicePlus;
import org.springframework.stereotype.Service;

@Service
public class NewOrderServiceImplPlus extends ServiceImpl<NewOrderMapperPlus, NewOrder> implements NewOrderServicePlus{
}
