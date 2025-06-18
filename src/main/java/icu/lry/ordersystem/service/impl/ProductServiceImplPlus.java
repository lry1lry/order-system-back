package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.ProductMapperPlus;
import icu.lry.ordersystem.pojo.Product;
import icu.lry.ordersystem.service.ProductServicePlus;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImplPlus extends ServiceImpl<ProductMapperPlus, Product> implements ProductServicePlus {
}
