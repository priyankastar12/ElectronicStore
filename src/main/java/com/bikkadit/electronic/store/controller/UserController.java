package com.bikkadit.electronic.store.controller;

import com.bikkadit.electronic.store.dto.ApiResponseMessage;
import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;




    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    // Create
          @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto)
    {

        logger.info("Initilizing createUser method");
        UserDto userDto1 = userService.createUser(userDto);
        logger.info("Execution completed of method createUser");
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    // Update
        @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
              @PathVariable("userId") String userId,
              @RequestBody UserDto userDto)
        {
            logger.info("Initilizing updateUser method for id:"+userId);
            UserDto updateUserDto = userService.updateUser(userDto,userId);
            logger.info("Execution completed of method updateUser for id:"+userId);
            return new ResponseEntity<>(updateUserDto,HttpStatus.OK);

        }// Delete
         @DeleteMapping
        public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)
         {
             logger.info("Initilizing deleteUser method for id:"+userId);
             userService.deleteUser(userId);
             ApiResponseMessage message = ApiResponseMessage
                     .builder()
                     .message("user Deleted Successfully!!")
                     .success(true)
                     .status(HttpStatus.OK)
                     .build();
             logger.info("Execution completed of method deleteUser for id:"+userId);
             return new ResponseEntity<>(message,HttpStatus.OK);


         }

         //Get all
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {



            logger.info("Initilizing getAllUsers method");
            return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);

        }
        // Get single
        @GetMapping("/{userId}")
        public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
            return new ResponseEntity<UserDto>(userService.getUserById(userId),HttpStatus.OK);



        }
        // get by email
        @GetMapping("/email/{email}")
        public ResponseEntity<UserDto> getUserByEmail (@PathVariable String email){
            return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
        }



        // Search
        @GetMapping("/search/{keywords}")
        public ResponseEntity<List<UserDto>> searchUser (@PathVariable String keywords)
        {
            logger.info("Initilizing searchUser method");
            return new ResponseEntity<List<UserDto>>(userService.searchUser(keywords), HttpStatus.OK);

        }
    }
