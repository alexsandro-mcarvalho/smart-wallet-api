package com.project.smart_wallet.mapper;

import com.project.smart_wallet.domain.CustomUser;
import com.project.smart_wallet.dto.response.UserResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {

    public static UserResponse toResponse(CustomUser user) {
        return new UserResponse(user.getName(), user.getUsername());
    }
}
