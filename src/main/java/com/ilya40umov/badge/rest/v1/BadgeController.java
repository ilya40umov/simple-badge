package com.ilya40umov.badge.rest.v1;

import com.ilya40umov.badge.rest.RestConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes REST API that works with badges.
 *
 * @author isorokoumov
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(RestConstants.V1_API_PREFIX)
public class BadgeController {

    // TODO add the following methods:
    // - list badges owned by user
    // - lookup badge by id
    // - lookup badge by title

    // - create badge (only badge creator role)
    // - update badge (only owner or admin)
    // - delete badge (only owner of admin)

    // - assign badge to user / take badge from user (owner or admin)
}
