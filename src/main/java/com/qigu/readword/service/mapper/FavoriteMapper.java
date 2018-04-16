package com.qigu.readword.service.mapper;

import com.qigu.readword.domain.*;
import com.qigu.readword.service.dto.FavoriteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Favorite and its DTO FavoriteDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, WordMapper.class})
public interface FavoriteMapper extends EntityMapper<FavoriteDTO, Favorite> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    FavoriteDTO toDto(Favorite favorite);

    @Mapping(source = "userId", target = "user")
    Favorite toEntity(FavoriteDTO favoriteDTO);

    default Favorite fromId(Long id) {
        if (id == null) {
            return null;
        }
        Favorite favorite = new Favorite();
        favorite.setId(id);
        return favorite;
    }
}
