package com.ilya40umov.badge.mapper;

import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import com.ilya40umov.badge.entity.AccountPrivilegeId;
import com.ilya40umov.badge.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Object mapper for {@link Account} entity.
 *
 * @author isorokoumov
 */
@Mapper(componentModel = "spring", uses = {AccountBadgeMapper.class})
public interface AccountMapper {

    @Mappings({
            @Mapping(source = "accountPrivileges", target = "privilegeIds")
    })
    AccountDto toAccountDto(Account account);

    default List<Integer> accountPrivilegesToIds(Set<AccountPrivilege> accountPrivileges) {
        return accountPrivileges.stream()
                .map(AccountPrivilege::getAccountPrivilegeId)
                .map(AccountPrivilegeId::getPrivilege)
                .map(Privilege::getPrivilegeId)
                .collect(Collectors.toList());
    }

}
