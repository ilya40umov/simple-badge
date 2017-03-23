package com.ilya40umov.badge.rest.v1;

import com.ilya40umov.badge.rest.RestConstants;
import com.ilya40umov.badge.service.exception.EntityNotFoundException;
import com.ilya40umov.badge.service.exception.InvalidRequestException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.springframework.security.test.web.servlet.request
        .SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@ControllerTest
public class ErrorHandlingTests {

    private static final String API_PREFIX = RestConstants.V1_API_PREFIX + "/failing/dummy/rest";

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping(API_PREFIX)
    public static class FailingController {

        @RequestMapping(value = "/not_found", method = RequestMethod.GET)
        public String entityNotFoundException() {
            throw new EntityNotFoundException();
        }

        @RequestMapping(value = "/invalid_request", method = RequestMethod.GET)
        public String invalidRequestException() {
            throw new InvalidRequestException();
        }

        @RequestMapping(value = "/sql_integrity_constraint_violation", method = RequestMethod.POST)
        public String sqlIntegrityConstraintViolationException() {
            // this is what exception looks like when unique constraint is violated
            throw new DataIntegrityViolationException("Integrity violated",
                    new ConstraintViolationException("Constraint violated",
                            new SQLIntegrityConstraintViolationException("Email taken"), ""));
        }

    }

    // TODO test for MethodArgumentNotValidException

    @Test
    public void entityNotFoundException_resultsInNotFoundStatus() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/not_found"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void invalidRequestException_resultsInBadRequestStatus() throws Exception {
        mockMvc.perform(get(API_PREFIX + "/invalid_request"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void sqlIntegrityConstraintViolationException_resultsInConflictStatus()
            throws Exception {
        mockMvc.perform(post(API_PREFIX + "/sql_integrity_constraint_violation").with(csrf()))
                .andExpect(status().isConflict());
    }
}
