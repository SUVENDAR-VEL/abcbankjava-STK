package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.UserRequestDto;
import com.abcbankfinal.abcbankweb.dto.UserResponseDto;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.response.ApiResponse;

public interface UserService {

    ApiResponse<User> createUserWithAccount(UserRequestDto request);


    ApiResponse<UserResponseDto> getUserById(Long userId);

    ApiResponse<Void> updateUser(Long userId, UserRequestDto request);
}
