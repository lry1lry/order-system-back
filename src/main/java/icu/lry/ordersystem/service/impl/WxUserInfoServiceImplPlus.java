package icu.lry.ordersystem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icu.lry.ordersystem.mapper.WxUserInfoMapperPlus;
import icu.lry.ordersystem.pojo.WxUserInfo;
import icu.lry.ordersystem.service.WxUserInfoServicePlus;
import org.springframework.stereotype.Service;

@Service
public class WxUserInfoServiceImplPlus extends ServiceImpl<WxUserInfoMapperPlus, WxUserInfo> implements WxUserInfoServicePlus {
}
