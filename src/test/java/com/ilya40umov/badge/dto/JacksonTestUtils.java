package com.ilya40umov.badge.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.boot.test.json.JacksonTester;

/**
 * @author isorokoumov
 */
class JacksonTestUtils {

    // XXX a hack, for more info see https://github.com/spring-projects/spring-boot/issues/8672
    static void useView(JacksonTester<?> jacksonTester, Class<?> view) {
        ObjectMapper objectMapper = (ObjectMapper) Whitebox.getInternalState(jacksonTester,
                "objectMapper");
        objectMapper = objectMapper.setConfig(objectMapper.getSerializationConfig().
                withView(view));
        Whitebox.setInternalState(jacksonTester, "objectMapper", objectMapper);
    }

}
