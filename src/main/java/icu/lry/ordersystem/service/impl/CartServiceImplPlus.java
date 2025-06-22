package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.CartMapperPlus;
import icu.lry.ordersystem.pojo.Cart;
import icu.lry.ordersystem.service.CartServicePlus;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplPlus extends ServiceImpl<CartMapperPlus, Cart> implements CartServicePlus {
}
