package com.dmdev.service;

import com.dmdev.dao.Dao;
import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import com.dmdev.validator.ValidationResult;
import org.h2.engine.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

import static com.dmdev.entity.Provider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private CreateSubscriptionMapper createSubscriptionMapper;

    @Mock
    private CreateSubscriptionValidator createSubscriptionValidator;

    @Mock
    private Clock clock;

    @Mock
    private SubscriptionDao subscriptionDao;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void updateSuccess() {
        CreateSubscriptionDto updateUserDto = getCreateSubscriptionDto();
        Subscription subscription = getSubscription();
        Subscription userSubscription = Subscription.builder()
                .id(1)
                .userId(1)
                .name("Petr")
                .provider(GOOGLE)
                .expirationDate(Instant.ofEpochSecond(90000000000000L))
                .status(Status.ACTIVE)
                .build();

        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(updateUserDto);
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(updateUserDto.getUserId());
        doReturn(userSubscription).when(subscriptionDao).upsert(subscription);

        Subscription actualResult = subscriptionService.upsert(updateUserDto);
        assertThat(actualResult).isEqualTo(userSubscription);


    }

    private Subscription getSubscription() {
        return Subscription.builder()
                .id(1)
                .userId(1)
                .name("Petr")
                .provider(GOOGLE)
                .expirationDate(Instant.ofEpochSecond(90000000000000L))
                .status(Status.ACTIVE)
                .build();
    }

    private CreateSubscriptionDto getCreateSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(1)
                .name("Petr")
                .provider(String.valueOf(GOOGLE))
                .expirationDate(Instant.now().plusSeconds(70))
                .build();
    }
}