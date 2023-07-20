package com.bikkadit.electronic.store.service;

import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.dto.UserDto;

import java.util.List;

public interface UserService {

      // Create user

    UserDto createUser(UserDto userDto);

    // update User
    UserDto updateUser(UserDto userDto,String userId);

    // delete User
    void deleteUser(String userId);

    // get all user
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single user by id

    UserDto getUserById(String userId);




    // get single user by email
    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUser(String keyword);
}
