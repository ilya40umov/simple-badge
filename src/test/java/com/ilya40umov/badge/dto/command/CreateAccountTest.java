package com.ilya40umov.badge.dto.command;

import com.ilya40umov.badge.dto.partial.AccountDetails;
import com.ilya40umov.badge.dto.partial.PasswordDetails;
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
public class CreateAccountTest {

    @Autowired
    private JacksonTester<CreateAccount> jacksonTester;

    @Test
    public void createAccount_isProperlyParsed() throws Exception {
        ObjectContent<CreateAccount> createAccount = jacksonTester.parse("{" +
                "\"account\":{\"email\":\"email@email.com\",\"firstName\":\"F.N.\"," +
                "\"lastName\":\"L.N.\"}," +
                "\"password\":{\"currentPassword\":\"\"," +
                "\"newPassword\":\"123abc\"}}");

        assertThat(createAccount).isEqualToComparingFieldByFieldRecursively(
                new CreateAccount()
                        .setAccount(new AccountDetails()
                                .setEmail("email@email.com").setFirstName("F.N.")
                                .setLastName("L.N."))
                        .setPassword(new PasswordDetails()
                                .setCurrentPassword("").setNewPassword("123abc")));
    }

}
