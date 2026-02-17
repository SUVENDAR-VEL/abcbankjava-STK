package com.abcbankfinal.abcbankweb.controller;



import com.abcbankfinal.abcbankweb.dto.*;
import com.abcbankfinal.abcbankweb.model.User;
import com.abcbankfinal.abcbankweb.response.ApiResponse;
import com.abcbankfinal.abcbankweb.service.UserAccountListProjection;
import com.abcbankfinal.abcbankweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<User>> saveUser(
            @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.createUserWithAccount(request));
    }


    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable Long userId) {

        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request) {
        return userService.login(request);
    }

    @PostMapping("/search")
    public Page<UserAccountListProjection> searchUsers(
            @RequestBody UserSearchRequest request) {
        return userService.searchUsers(request);
    }

}

