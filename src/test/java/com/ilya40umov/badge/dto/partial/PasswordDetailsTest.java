package com.ilya40umov.badge.dto.partial;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author isorokoumov
 */
@RunWith(SpringRunner.class)
@JsonTest
public class PasswordDetailsTest {

    @Autowired
    private JacksonTester<PasswordDetails> jacksonTester;

    @Test
    public void passwordDetails_isProperlyParsed() throws Exception {
        ObjectContent<PasswordDetails> passwordDetails = jacksonTester.parse(
                "{\"currentPassword\":\"abc123\",\"newPassword\":\"123abc\"}");

        assertThat(passwordDetails).isEqualToComparingFieldByField(
                new PasswordDetails().setCurrentPassword("abc123").setNewPassword("123abc"));
    }
}
