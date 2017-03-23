package com.ilya40umov.badge.rest.v1;

import com.ilya40umov.badge.rest.RestConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static com.ilya40umov.badge.rest.v1.ControllerTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ControllerTest
public class AnnotationSecurityTests {

    private static final String API_PREFIX = RestConstants.V1_API_PREFIX + "/secured/dummy/rest";

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping(API_PREFIX)
    public static class AnnotationSecuredController {

        @PreAuthorize("permitAll()")
        @RequestMapping(value = "/permit_all", method = RequestMethod.GET)
        public Map<String, ?> permitAll() {
            return Collections.singletonMap("status", HttpStatus.OK.value());
        }

        @PreAuthorize("isAuthenticated()")
        @RequestMapping(value = "/permit_authenticated", method = RequestMethod.GET)
        public Map<String, ?> permitAuthenticated() {
            return Collections.singletonMap("status", HttpStatus.OK.value());
        }

        @PreAuthorize("@securityChecks.isCurrentAccountOrAdmin(#accountId)")
        @RequestMapping(value = "/permit_current_account_or_admin", method = RequestMethod.GET)
        public Map<String, ?> permitCurrentAccountOrAdmin(
                @RequestParam("account_id") Long accountId) {
            return Collections.singletonMap("status", HttpStatus.OK.value());
        }

        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @RequestMapping(value = "/permit_admin", method = RequestMethod.GET)
        public Map<String, ?> permitAdmin() {
            return Collections.singletonMap("status", HttpStatus.OK.value());
        }
    }

    @Test
    public void permitAll_isAccessible_ifNotAuthenticated() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void permitAll_isAccessible_ifAuthenticated() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_all"))
                .andExpect(status().isOk());
    }

    @Test
    public void permitAuthenticated_isNotAccessible_ifNotAuthenticated() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_authenticated"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void permitAuthenticated_isAccessible_ifAuthenticated() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_authenticated"))
                .andExpect(status().isOk());
    }

    @Test
    public void permitCurrentAccountOrAdmin_isNotAccessible_ifNotAuthenticated() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_current_account_or_admin")
                .param("account_id", String.valueOf(ACCOUNT_1_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ACCOUNT_2_LOGIN)
    public void permitCurrentAccountOrAdmin_isNotAccessible_ifNotCurrentAccount() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_current_account_or_admin")
                .param("account_id", String.valueOf(ACCOUNT_1_ID)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void permitCurrentAccountOrAdmin_isAccessible_ifCurrentAccount() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_current_account_or_admin")
                .param("account_id", String.valueOf(ACCOUNT_1_ID)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(ADMIN_LOGIN)
    public void permitCurrentAccountOrAdmin_isAccessible_ifAdmin() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_current_account_or_admin")
                .param("account_id", String.valueOf(ACCOUNT_1_ID)))
                .andExpect(status().isOk());
    }

    @Test
    public void permitAdmin_isNotAccessible_ifNotAuthenticated() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ACCOUNT_1_LOGIN)
    public void permitAdmin_isNotAccessible_ifAuthenticatedAsNotAdmin() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(ADMIN_LOGIN)
    public void permitAdmin_isAccessible_ifAdmin() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/permit_admin"))
                .andExpect(status().isOk());
    }
}
