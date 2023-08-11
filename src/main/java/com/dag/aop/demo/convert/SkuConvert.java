package com.dag.aop.demo.convert;


import com.dag.aop.demo.domain.dto.SkuDTO;
import com.dag.aop.demo.domain.po.Sku;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SkuConvert {
    SkuConvert INSTANCE = Mappers.getMapper( SkuConvert.class );

    SkuDTO toDto(Sku sku);
}
