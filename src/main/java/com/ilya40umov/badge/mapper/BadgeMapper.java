package com.ilya40umov.badge.mapper;

import com.ilya40umov.badge.dto.BadgeDto;
import com.ilya40umov.badge.entity.Badge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Object mapper for {@link Badge} entity.
 *
 * @author isorokoumov
 */
@Mapper(componentModel = "spring")
public interface BadgeMapper {

    @Mappings({
            @Mapping(source = "owner.accountId", target = "ownerId")
    })
    BadgeDto toBadgeDto(Badge badge);

}
