package com.ilya40umov.badge;

import com.google.common.collect.ImmutableMap;
import com.ilya40umov.badge.dto.AccountDto;
import com.ilya40umov.badge.dto.command.CreateAccount;
import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;

import java.util.Collections;
import java.util.Map;

import static com.ilya40umov.badge.rest.RestConstants.V1_API_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption
        .ENABLE_COOKIES;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimpleBadgeApplicationIntegrationTests {

    private static final String EMAIL = "user@gmail.com";
    private static final String PASSWORD = "qwerty123";

    @LocalServerPort
    private Integer port;

    private TestRestTemplate restTemplate;

    @Before
    public void createRestTemplate() throws Exception {
        DefaultUriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();
        uriTemplateHandler.setBaseUrl("http://localhost:" + port);
        RestTemplate innerTemplate = new RestTemplate();
        innerTemplate.setUriTemplateHandler(uriTemplateHandler);
        restTemplate = new TestRestTemplate(innerTemplate, null, null, ENABLE_COOKIES);
        // asserting that session is actually preserved between requests
        Map<?, ?> csrf1Response = restTemplate.getForObject("/api/csrf", Map.class);
        Map<?, ?> csrf2Response = restTemplate.getForObject("/api/csrf", Map.class);
        assertThat(csrf1Response.get("token")).isEqualTo(csrf2Response.get("token"));
    }

    private <T> HttpEntity<T> withCsrfHeader(T body, String token) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put("X-CSRF-TOKEN", Collections.singletonList(token));
        return new HttpEntity<>(body, headers);
    }

    @Test
    public void apiShouldAllowToCreateAccountThenLoginIntoItAndUpdateItsInfo() {
        Map<?, ?> csrfResponse = restTemplate.getForObject("/api/csrf", Map.class);
        String token = (String) csrfResponse.get("token");
        assertThat(token).isNotNull();

        ResponseEntity<AccountDto> createResponse = restTemplate.postForEntity(
                V1_API_PREFIX + "/account",
                withCsrfHeader(
                        new CreateAccount()
                                .setAccount(new AccountDetails()
                                        .setEmail(EMAIL)
                                        .setFirstName("FistName")
                                        .setLastName("LastName"))
                                .setPassword(new PasswordDetails()
                                        .setNewPassword(PASSWORD)),
                        token),
                AccountDto.class
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody().getAccountId()).isNotNull();

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity("/api/login",
                withCsrfHeader(
                        ImmutableMap.of("username", EMAIL, "password", PASSWORD),
                        token
                ),
                Map.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        token = (String) ((Map) loginResponse.getBody().get("csrfInfo")).get("token");

        ResponseEntity<Map> updateResponse = restTemplate.postForEntity(
                V1_API_PREFIX + "/account/" + createResponse.getBody().getAccountId(),
                withCsrfHeader(
                        new AccountDetails()
                                .setEmail(EMAIL)
                                .setFirstName("NewFirstName")
                                .setLastName("NewLastName"),
                        token),
                Map.class
        );
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<AccountDto> getResponse = restTemplate.getForEntity(
                V1_API_PREFIX + "/account/" + createResponse.getBody().getAccountId(),
                AccountDto.class
        );
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getFirstName()).isEqualTo("NewFirstName");
    }

}