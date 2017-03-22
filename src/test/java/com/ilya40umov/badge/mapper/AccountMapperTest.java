package com.ilya40umov.badge.mapper;

import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.entity.Account;
import com.ilya40umov.badge.entity.AccountPrivilege;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import static com.ilya40umov.badge.entity.Privilege.ADMINISTER;
import static com.ilya40umov.badge.entity.Privilege.CREATE_BADGE;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class AccountMapperTest {

    private static final Long ACCOUNT_ID = 1234L;
    private static final String EMAIL = "email@gmail.com";
    private static final String FIRST_NAME = "F.N.";
    private static final String LAST_NAME = "L.N.";


    @Autowired
    private AccountMapper mapper;

    @Test
    public void toAccountDto_transformsEntityToDto() throws Exception {
        AccountDto dto = mapper.toAccountDto(new Account()
                .setAccountId(ACCOUNT_ID)
                .setEmail(EMAIL)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setCreated(Instant.EPOCH)
                .setModified(Instant.EPOCH)
                .addAccountPrivilege(AccountPrivilege.fromPrivilege(ADMINISTER))
                .addAccountPrivilege(AccountPrivilege.fromPrivilege(CREATE_BADGE)));

        assertThat(dto).isEqualToComparingFieldByField(new AccountDto()
                .setAccountId(ACCOUNT_ID)
                .setEmail(EMAIL)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setCreated(Instant.EPOCH)
                .setModified(Instant.EPOCH)
                .setPrivilegeIds(Arrays.asList(ADMINISTER.getPrivilegeId(),
                        CREATE_BADGE.getPrivilegeId()))
                .setAccountBadges(Collections.emptyList()));
    }
}
