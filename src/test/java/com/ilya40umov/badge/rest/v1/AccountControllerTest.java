package com.ilya40umov.badge.rest.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.dto.command.CreateAccount;
import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
import com.ilya40umov.badge.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static com.ilya40umov.badge.rest.RestConstants.V1_API_PREFIX;
import static com.ilya40umov.badge.rest.v1.ControllerTest.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.test.web.servlet.request
        .SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ControllerTest
public class AccountControllerTest {

    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Johnson";
    private static final String VALID_PASSWORD = "qwerty123";
    private static final String INVALID_PASSWORD = "qwerty\t";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountService accountService;

    @Test
    @WithUserDetails(ACCOUNT_2_LOGIN)
    public void findById_returnsPublicView_ifNotOwnerAndNotAdmin() throws Exception {
        when(accountService.findById(ACCOUNT_1_ID))
                .thenReturn(Optional.of(new AccountDto()
                        .setEmail(ACCOUNT_1_LOGIN)
                        .setFirstName(FIRST_NAME)));

        mockMvc.perform(get(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").doesNotExist())
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void findById_returnsPrivateView_ifOwner() throws Exception {
        when(accountService.findById(ACCOUNT_1_ID))
                .thenReturn(Optional.of(new AccountDto()
                        .setEmail(ACCOUNT_1_LOGIN)
                        .setFirstName(FIRST_NAME)));

        mockMvc.perform(get(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(ACCOUNT_1_LOGIN))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    @WithUserDetails(ADMIN_LOGIN)
    public void findById_returnsPrivateView_ifAdmin() throws Exception {
        when(accountService.findById(ACCOUNT_1_ID))
                .thenReturn(Optional.of(new AccountDto()
                        .setEmail(ACCOUNT_1_LOGIN)
                        .setFirstName(FIRST_NAME)));

        mockMvc.perform(get(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(ACCOUNT_1_LOGIN))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    public void findById_returns404_ifAccountNotFound() throws Exception {
        when(accountService.findById(ACCOUNT_1_ID)).thenReturn(Optional.empty());

        mockMvc.perform(get(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(NOT_FOUND.value()));
    }

    @Test
    public void isEmailRegistered_tellsIfEmailIsAlreadyRegistered() throws Exception {
        when(accountService.isEmailRegistered(ACCOUNT_1_LOGIN)).thenReturn(true);

        mockMvc.perform(
                get(V1_API_PREFIX + "/account/email_registered")
                        .param("email", ACCOUNT_1_LOGIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registered").value(true));
    }

    private CreateAccount newCreateAccount(String email, String password) throws Exception {
        return new CreateAccount()
                .setAccount(new AccountDetails()
                        .setEmail(email)
                        .setFirstName(FIRST_NAME)
                        .setLastName(LAST_NAME))
                .setPassword(new PasswordDetails()
                        .setNewPassword(password));
    }

    @Test
    public void createAccount_createsAccountAndReturnsPrivateViewOfAccount() throws Exception {
        when(accountService.createAccount(any()))
                .thenReturn(new AccountDto()
                        .setEmail(ACCOUNT_1_LOGIN)
                        .setFirstName(FIRST_NAME)
                        .setLastName(LAST_NAME));
        CreateAccount createAccount = newCreateAccount(ACCOUNT_1_LOGIN, VALID_PASSWORD);

        mockMvc.perform(
                post(V1_API_PREFIX + "/account").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(ACCOUNT_1_LOGIN))
                .andExpect(jsonPath("$.firstName").value(FIRST_NAME));
    }

    @Test
    public void createAccount_returnsValidationErrors_forInvalidJson() throws Exception {
        CreateAccount createAccount = newCreateAccount("invalid email", INVALID_PASSWORD);

        mockMvc.perform(
                post(V1_API_PREFIX + "/account").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createAccount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.fieldErrors", hasSize(2)));

        verifyZeroInteractions(accountService);
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void updateAccount_updatesAccountAndReturnsOkStatus_ifAccountFound() throws Exception {
        doNothing().when(accountService).updateAccount(anyLong(), any());
        AccountDetails accountDetails = new AccountDetails()
                .setEmail(ACCOUNT_1_LOGIN)
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME);

        mockMvc.perform(
                post(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(accountDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.value()));
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void updatePassword_returnsOkStatus_ifUpdateSucceeded() throws Exception {
        doNothing().when(accountService).updatePassword(anyLong(), any());

        mockMvc.perform(
                post(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID + "/change_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new PasswordDetails().setNewPassword(VALID_PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.value()));
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void updatePassword_returnsBadRequestStatus_ifNewPasswordIsInvalid() throws Exception {
        mockMvc.perform(
                post(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID + "/change_password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(
                                new PasswordDetails().setNewPassword(INVALID_PASSWORD))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(BAD_REQUEST.value()))
                .andExpect(jsonPath("$.fieldErrors", hasSize(1)));
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void deleteAccount_returnsOkStatus_ifAccountFoundAndDeleteSucceeded() throws Exception {
        doNothing().when(accountService).deleteAccount(anyLong());

        mockMvc.perform(
                delete(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.value()));
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void listWithBadgeId_returnsAccountsUnderPublicView() throws Exception {
        when(accountService.listWithBadgeId(anyLong(), any()))
                .thenReturn(new PageImpl<>(Collections.singletonList(
                        new AccountDto()
                                .setEmail(ACCOUNT_1_LOGIN)
                                .setFirstName(FIRST_NAME))));

        mockMvc.perform(
                get(V1_API_PREFIX + "/accounts/list_with_badge")
                        .param("badge_id", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").doesNotExist())
                .andExpect(jsonPath("$.content[0].firstName").value(FIRST_NAME));
    }

    @Test
    @WithUserDetails(ADMIN_LOGIN)
    public void addPrivilege_returnsOkStatus_ifOperationSucceeded() throws Exception {
        mockMvc.perform(
                post(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID + "/add_privilege")
                        .param("privilege_id", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.value()));
    }

    @Test
    @WithUserDetails(ADMIN_LOGIN)
    public void removePrivilege_returnsOkStatus_ifOperationSucceeded() throws Exception {
        mockMvc.perform(
                post(V1_API_PREFIX + "/account/" + ACCOUNT_1_ID + "/remove_privilege")
                        .param("privilege_id", "1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OK.value()));
    }

}
