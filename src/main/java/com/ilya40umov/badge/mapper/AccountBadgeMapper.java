package com.ilya40umov.badge.mapper;

import com.ilya40umov.badge.dto.AccountBadgeDto;
import com.ilya40umov.badge.entity.AccountBadge;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;

/**
 * Object mapper for {@link AccountBadge} entity.
 *
 * @author isorokoumov
 */
@Mapper(componentModel = "spring", uses = {BadgeMapper.class})
public interface AccountBadgeMapper {

    @Mappings({
            @Mapping(source = "created", target = "assigned")
    })
    AccountBadgeDto toAccountBadgeDto(AccountBadge accountBadge);

    List<AccountBadgeDto> toAccountBadgeDtos(Set<AccountBadge> accountBadges);

}
