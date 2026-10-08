package com.formcrafter.auth.user;

import com.formcrafter.auth.user.dtos.requests.CreateUserRequest;
import com.formcrafter.auth.user.dtos.responses.UserDTO;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private static final Long USER_ID = 1L;
    private static final String EMAIL = "adel_elsawaf@example.com";
    private static final String FIRST_NAME = "Adel";
    private static final String LAST_NAME = "Elsawaf";

    private final UserMapper userMapper = new UserMapper();

    @Nested
    class ToEntity {

        @Test
        void mapsCreateUserRequestFields() {
            CreateUserRequest request = CreateUserRequest.builder()
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .email(EMAIL)
                    .build();

            UserEntity actual = userMapper.toEntity(request);

            assertThat(actual).usingRecursiveComparison().isEqualTo(
                    UserEntity.builder()
                            .firstName(FIRST_NAME)
                            .lastName(LAST_NAME)
                            .email(EMAIL)
                            .build()
            );
        }
    }

    @Nested
    class ToDto {

        @Test
        void mapsUserEntityFields() {
            UserEntity entity = UserEntity.builder()
                    .id(USER_ID)
                    .firstName(FIRST_NAME)
                    .lastName(LAST_NAME)
                    .email(EMAIL)
                    .build();

            UserDTO actual = userMapper.toDto(entity);

            assertThat(actual).usingRecursiveComparison().isEqualTo(
                    UserDTO.builder()
                            .id(USER_ID)
                            .firstName(FIRST_NAME)
                            .lastName(LAST_NAME)
                            .email(EMAIL)
                            .build()
            );
        }
    }
}
