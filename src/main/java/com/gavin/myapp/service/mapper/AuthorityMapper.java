package com.gavin.myapp.service.mapper;

import com.gavin.myapp.domain.*;
import com.gavin.myapp.service.dto.AuthorityDTO;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ViewPermissionMapper.class, AuthoritySimpleMapper.class })
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {
    @Mapping(source = "parent", target = "parent")
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "viewPermissions", ignore = true)
    AuthorityDTO toDto(Authority authority);

    @Named("noChildren")
    @Mapping(source = "parent", target = "parent")
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "viewPermissions", ignore = true)
    AuthorityDTO toNoChildrenDto(Authority authority);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    @Mapping(target = "viewPermissions", ignore = true)
    @Mapping(target = "removeViewPermissions", ignore = true)
    @Mapping(target = "apiPermissions", ignore = true)
    @Mapping(target = "removeApiPermissions", ignore = true)
    @Mapping(target = "users", ignore = true)
    Authority toEntity(AuthorityDTO authorityDTO);

    default Authority fromId(Long id) {
        if (id == null) {
            return null;
        }
        Authority authority = new Authority();
        authority.setId(id);
        return authority;
    }

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    default AuthorityDTO toDtoName(Authority authority) {
        if (authority == null) {
            return null;
        }
        AuthorityDTO authorityDTO = new AuthorityDTO();
        authorityDTO.setId(authority.getId());
        authorityDTO.setName(authority.getName());
        return authorityDTO;
    }

    @Named("nameList")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = "code")
    default ArrayList<AuthorityDTO> toDtoNameList(List<Authority> authorities) {
        if (authorities == null) {
            return null;
        }
        ArrayList<AuthorityDTO> authorityList = new ArrayList<>();
        for (Authority authorityEntity : authorities) {
            authorityList.add(this.toDtoName(authorityEntity));
        }
        return authorityList;
    }
}
