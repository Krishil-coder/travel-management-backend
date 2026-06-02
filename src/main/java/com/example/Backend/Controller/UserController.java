package com.example.Backend.Controller;

import com.example.Backend.DTO.Request.CreateUserDto;
import com.example.Backend.DTO.Request.UpdateUserDto;
import com.example.Backend.DTO.Response.ApiResponse;
import com.example.Backend.DTO.Response.UserResponse;
import com.example.Backend.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody CreateUserDto dto
            ){
        UserResponse Response = userService.createUser(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(Response, "User Created Successfully"));
    }

    @GetMapping()
        public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<String>>> getDepartments() {
        List<String> response = userService.getDepartments();

        return ResponseEntity.ok(
                ApiResponse.success(response, "Departments Fetched Successfully")
        );
    }

    @GetMapping("/managers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getManagersByDepartment(
            @RequestParam String department
    ) {
        List<UserResponse> response = userService.getManagersByDepartment(department);

        return ResponseEntity.ok(
                ApiResponse.success(response, "Managers Fetched Successfully")
        );
    }

    @GetMapping("/{id:\\d+}")
    public List<UserResponse> getUserById(
            @PathVariable Long id){

        return userService.getUsersById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDto dto
    ) {
        UserResponse response = userService.updateUser(id, dto);

        return ResponseEntity
                .ok(ApiResponse.success(response, "User Updated Successfully"));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(
            @PathVariable Long id){

        userService.Delete(id);

    }

}
