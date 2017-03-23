package com.ilya40umov.badge.rest.v1;

import com.fasterxml.jackson.annotation.JsonView;
import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.dto.Views;
import com.ilya40umov.badge.dto.command.CreateAccount;
import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
import com.ilya40umov.badge.rest.RestConstants;
import com.ilya40umov.badge.security.SecurityChecks;
import com.ilya40umov.badge.service.AccountService;
import com.ilya40umov.badge.service.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

/**
 * Exposes REST API that works with accounts.
 *
 * @author isorokoumov
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = RestConstants.V1_API_PREFIX)
public class AccountController {

    private final AccountService accountService;
    private final SecurityChecks securityChecks;

    public AccountController(AccountService accountService, SecurityChecks securityChecks) {
        this.accountService = accountService;
        this.securityChecks = securityChecks;
    }

    /**
     * Looks up an account by ID and depending on the type of the user accessing the data,
     * returns either information that is available publicly or all information about the account.
     */
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/account/{accountId}", method = RequestMethod.GET)
    public MappingJacksonValue findById(@PathVariable("accountId") Long accountId) {
        AccountDto accountDto = accountService.findById(accountId)
                .orElseThrow(EntityNotFoundException::new);
        MappingJacksonValue jacksonValue = new MappingJacksonValue(accountDto);
        if (securityChecks.isCurrentAccountOrAdmin(accountId)) {
            jacksonValue.setSerializationView(Views.Private.class);
        } else {
            jacksonValue.setSerializationView(Views.Public.class);
        }
        return jacksonValue;
    }

    /**
     * Tells if a provided email is already registered.
     */
    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/account/email_registered", method = RequestMethod.GET)
    @Validated
    public Map<String, ?> isEmailRegistered(@RequestParam("email") String email) {
        return Collections.singletonMap("registered", accountService.isEmailRegistered(email));
    }

    /**
     * Creates a new account from the provided information.
     */
    @PreAuthorize("permitAll()")
    @JsonView(Views.Private.class)
    @RequestMapping(value = "/account", method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@Valid @RequestBody CreateAccount createAccount) {
        return accountService.createAccount(createAccount);
    }

    /**
     * Updates the account data using the provided information.
     */
    @PreAuthorize("@securityChecks.isCurrentAccountOrAdmin(#accountId)")
    @RequestMapping(value = "/account/{accountId}", method = RequestMethod.POST,
            consumes = "application/json")
    public Map<String, ?> updateAccount(@PathVariable("accountId") Long accountId,
                                        @Valid @RequestBody AccountDetails accountDetails) {
        accountService.updateAccount(accountId, accountDetails);
        return Collections.singletonMap("status", HttpStatus.OK.value());
    }

    /**
     * Updates password for the account.
     */
    @PreAuthorize("@securityChecks.isCurrentAccountOrAdmin(#accountId)")
    @RequestMapping(value = "/account/{accountId}/change_password", method = RequestMethod.POST,
            consumes = "application/json")
    public Map<String, ?> updatePassword(@PathVariable("accountId") Long accountId,
                                         @Valid @RequestBody PasswordDetails passwordDetails) {
        accountService.updatePassword(accountId, passwordDetails);
        return Collections.singletonMap("status", HttpStatus.OK.value());
    }

    /**
     * Deletes the account.
     */
    // TODO it would also make sense to ask for a password or "fully authenticated user"
    @PreAuthorize("@securityChecks.isCurrentAccountOrAdmin(#accountId)")
    @JsonView(Views.Private.class)
    @RequestMapping(value = "/account/{accountId}", method = RequestMethod.DELETE)
    public Map<String, ?> deleteAccount(@PathVariable("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
        return Collections.singletonMap("status", HttpStatus.OK.value());
    }

    /**
     * Lists accounts that have a specific badge on them.
     */
    @PreAuthorize("isAuthenticated()")
    @JsonView(Views.Public.class)
    @RequestMapping(value = "/accounts/list_with_badge", method = RequestMethod.GET)
    public Page<AccountDto> listWithBadgeId(@RequestParam("badge_id") Long badgeId,
                                            @PageableDefault Pageable pageable) {
        return accountService.listWithBadgeId(badgeId, pageable);
    }

    /**
     * Grants the privilege identified by a given {@code privilegeId} to the account.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/account/{accountId}/add_privilege", method = RequestMethod.POST)
    public Map<String, ?> addPrivilege(@PathVariable("accountId") Long accountId,
                                       @RequestParam("privilege_id") Integer privilegeId) {
        accountService.addPrivilege(accountId, privilegeId);
        return Collections.singletonMap("status", HttpStatus.OK.value());
    }

    /**
     * Takes away the privilege identified by a given {@code privilegeId} from the account.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/account/{accountId}/remove_privilege", method = RequestMethod.POST)
    public Map<String, ?> removePrivilege(@PathVariable("accountId") Long accountId,
                                          @RequestParam("privilege_id") Integer privilegeId) {
        accountService.removePrivilege(accountId, privilegeId);
        return Collections.singletonMap("status", HttpStatus.OK.value());
    }

}
