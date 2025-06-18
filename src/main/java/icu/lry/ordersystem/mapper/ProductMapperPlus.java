package icu.lry.ordersystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import icu.lry.ordersystem.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapperPlus extends BaseMapper<Product> {
}
