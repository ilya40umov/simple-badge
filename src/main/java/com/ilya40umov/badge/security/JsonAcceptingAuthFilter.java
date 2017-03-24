package com.ilya40umov.badge.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This authentication filter extends Spring's implementation adding support for JSON credentials.
 *
 * @author isorokoumov
 */
public class JsonAcceptingAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    static class AuthData {

        private String username;
        private String password;

        String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        if (request.getHeader(HttpHeaders.CONTENT_TYPE) == null ||
                !request.getHeader(HttpHeaders.CONTENT_TYPE).contains(APPLICATION_JSON_VALUE)) {
            return super.attemptAuthentication(request, response);
        } else {
            try {
                AuthData authData = objectMapper.readValue(request.getReader(), AuthData.class);
                if (authData == null) {
                    throw new AuthenticationServiceException(
                            "Failed to find authData in the request");
                }
                return new UsernamePasswordAuthenticationToken(authData.getUsername(),
                        authData.getPassword());
            } catch (Exception e) {
                throw new AuthenticationServiceException(
                        "Failed to parse authentication request body");
            }
        }
    }

}
