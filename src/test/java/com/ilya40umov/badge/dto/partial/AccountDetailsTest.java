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
public class AccountDetailsTest {

    @Autowired
    private JacksonTester<AccountDetails> jacksonTester;

    @Test
    public void accountDetails_isProperlyParsed() throws Exception {
        ObjectContent<AccountDetails> accountDetails = jacksonTester.parse(
                "{\"email\":\"email@email.com\",\"firstName\":\"F.N.\",\"lastName\":\"L.N.\"}");

        assertThat(accountDetails).isEqualToComparingFieldByField(
                new AccountDetails().setEmail("email@email.com")
                        .setFirstName("F.N.").setLastName("L.N."));
    }
}
