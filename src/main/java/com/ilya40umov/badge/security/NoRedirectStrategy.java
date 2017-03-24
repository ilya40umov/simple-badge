package com.ilya40umov.badge.security;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A redirect strategy that results in no redirect.
 *
 * @author isorokoumov
 */
public class NoRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect(HttpServletRequest request,
                             HttpServletResponse response,
                             String url) throws IOException {
        // no redirect
    }

}
