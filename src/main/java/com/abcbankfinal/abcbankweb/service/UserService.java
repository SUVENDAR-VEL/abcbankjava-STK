package com.abcbankfinal.abcbankweb.service;

import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    ApiResponse<User> createUserWithAccount(UserRequestDto request);

    ApiResponse<UserResponseDto> getUserById(Long userId);

    ApiResponse<Void> updateUser(Long userId, UserRequestDto request);

    ApiResponse<LoginResponseDTO> login(LoginRequestDTO request);

    Page<UserAccountListProjection> searchUsers(UserSearchRequest request);

    ApiResponse<UserOnlyResponseDto> getUserOnlyById(Long userId);
}