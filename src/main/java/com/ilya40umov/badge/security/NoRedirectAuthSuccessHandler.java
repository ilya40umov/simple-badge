package com.ilya40umov.badge.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Prevents redirect on successful authentication and instead replies with a new CSRF token.
 *
 * @author isorokoumov
 */
@Component
public class NoRedirectAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public NoRedirectAuthSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        setRedirectStrategy(new NoRedirectStrategy());

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        response.setContentType("application/json");
        response.getWriter().print(
                objectMapper.writeValueAsString(Collections.singletonMap("csrfInfo", token)));
        response.getWriter().flush();
    }

}
