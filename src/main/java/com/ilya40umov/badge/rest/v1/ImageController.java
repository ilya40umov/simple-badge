package com.ilya40umov.badge.rest.v1;

import com.ilya40umov.badge.rest.RestConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Exposes REST API that works with badge images.
 *
 * @author isorokoumov
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(RestConstants.V1_API_PREFIX)
public class ImageController {

    // TODO add methods to: upload temporary image, assign tmp image to badge, get image for badge

}
