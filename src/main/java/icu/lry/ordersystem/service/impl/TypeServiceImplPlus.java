package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.TypeMapperPlus;
import icu.lry.ordersystem.pojo.Type;
import icu.lry.ordersystem.service.TypeServicePlus;
import org.springframework.stereotype.Service;

@Service
public class TypeServiceImplPlus extends ServiceImpl<TypeMapperPlus, Type> implements TypeServicePlus {
}
