package com.dmdev.validator;

import com.dmdev.dto.CreateSubscriptionDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.dmdev.entity.Provider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateSubscriptionValidatorTest {

    private final CreateSubscriptionValidator validator = CreateSubscriptionValidator.getInstance();

    @Test
    void shouldPassValidation() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Petr")
                .provider(String.valueOf(GOOGLE))
                .expirationDate(Instant.now().plusSeconds(70))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertFalse(actualResult.hasErrors());
    }

    @Test
    void invalidUserId() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name("Petr")
                .provider(String.valueOf(GOOGLE))
                .expirationDate(Instant.now().plusSeconds(70))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(100);
    }

    @Test
    void invalidUserName() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name(" ")
                .provider(String.valueOf(GOOGLE))
                .expirationDate(Instant.now().plusSeconds(70))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(101);
    }

    @Test
    void invalidateProvider() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Petr")
                .provider("PFFFFF")
                .expirationDate(Instant.now().plusSeconds(70))
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(102);

    }

    @Test
    void invalidateExpirationDate() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(1)
                .name("Petr")
                .provider(String.valueOf(GOOGLE))
                .expirationDate(Instant.now())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo(103);
    }

    @Test
    void invalidateUserIdUserNameProviderExpirationDate() {
        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(null)
                .name(" ")
                .provider("PFFFF")
                .expirationDate(Instant.now())
                .build();

        ValidationResult actualResult = validator.validate(dto);
        List<Integer> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(errorCodes).contains(100, 101, 102, 103);
    }

}