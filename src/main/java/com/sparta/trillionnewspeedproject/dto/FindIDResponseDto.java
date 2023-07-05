package com.sparta.trillionnewspeedproject.dto;

import com.sparta.trillionnewspeedproject.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class FindIDResponseDto extends ApiResponseDto{
    String userId;
        public FindIDResponseDto(User user){
            this.userId = user.getUserId();

        }
}
