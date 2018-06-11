package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.VipOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity VipOrder and its DTO VipOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, UserMapper.class})
public interface VipOrderMapper extends EntityMapper<VipOrderDTO, VipOrder> {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    VipOrderDTO toDto(VipOrder vipOrder);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "userId", target = "user")
    VipOrder toEntity(VipOrderDTO vipOrderDTO);

    default VipOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        VipOrder vipOrder = new VipOrder();
        vipOrder.setId(id);
        return vipOrder;
    }
}
