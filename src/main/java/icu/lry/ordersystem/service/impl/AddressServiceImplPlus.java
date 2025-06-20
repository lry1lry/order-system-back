package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.AddressMapperPlus;
import icu.lry.ordersystem.pojo.Address;
import icu.lry.ordersystem.service.AddressServicePlus;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImplPlus extends ServiceImpl<AddressMapperPlus, Address> implements AddressServicePlus {
}
