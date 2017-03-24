package com.ilya40umov.badge.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides the endpoint for obtaining CSRF token.
 *
 * @author isorokoumov
 */
@Controller
public class CsrfTokenController {

    @PreAuthorize("permitAll()")
    @RequestMapping("/api/csrf")
    @ResponseBody
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

}
