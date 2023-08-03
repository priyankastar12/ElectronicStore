package com.bikkadit.electronic.store.service;


import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.entity.User;
import com.bikkadit.electronic.store.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;


    @Autowired
    private UserService userService;

    User user;
    @BeforeEach
    public void init() {

     user= User.builder()
                .name("Priyanka")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();
    }



    // create user
    @Test
    public void createUserTest(){
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto user1=    userService.createUser(mapper.map(user, UserDto.class));
        System.out.println(user1.getName());
        Assertions.assertNotNull(user1);
        Assertions.assertEquals("Priyanka",user1.getName());
    }
   // update user
    @Test
    public void updateUserTest(){
        String userId="dggfuifgbjj";
        UserDto userDto=UserDto.builder()
               .name("Priyanka Pawar")
                .about("this is updated user about details")
                .gender("female")
                .imageName("xyz.png")
                .build();
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto updatedUser= userService.updateUser(userDto,userId);
        System.out.println(updatedUser.getName());
        System.out.println(updatedUser.getImageName());
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(userDto.getName(),updatedUser.getName(),"Name is not validated");
    }
     // Delete user test case
    @Test
    public void deleteUserTest(){
        String userid="userIdabc";
        Mockito.when(userRepository.findById("userIdabc")).thenReturn(Optional.of(user));
        userService.deleteUser(userid);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }

    @Test
    public void getAllUsersTest() {
    User  user1= User.builder()
                .name("Nikita")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();

     User user2= User.builder()
                .name("Shital")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();

        List<User> userList= Arrays.asList(user,user1,user2);
        Page<User> page=new PageImpl<>(userList);

        Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        
        PageableResponse<UserDto> alluser= userService.getAllUser(1,2,"name","asc");
        Assertions.assertEquals(3,alluser.getContent().size());
    }

    @Test
    public void getUserByIdTest(){
        String userId="userIdTest";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // actual call of service method
        UserDto userDto=  userService.getUserById(userId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(),userDto.getName(),"name not matched !!");


    }
    // get user By Email
    @Test
    public void getUserByEmailTest(){
        String emailId="priyanka@gmail.com";
        Mockito.when(userRepository.findByEmail(emailId)).thenReturn(Optional.of(user));
        UserDto userDto=userService.getUserByEmail(emailId);
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getEmail(),userDto.getEmail(),"Email Not Matched !!");


    }
    // search user
    @Test
    public void searchUserTest(){
        User user1=User.builder()
                .name("Priyanka Pawar ")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();

        User user2= User.builder()
                .name("Ankita Donde ")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();

        User user3= User.builder()
                .name("Shital Pawar")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();

        String keywords="Pawar";

        Mockito.when(userRepository.findByNameContaining(keywords)).thenReturn(Arrays.asList(user,user1,user2,user3));

        List<UserDto>  userDtos= userService.searchUser(keywords);
        Assertions.assertEquals(4,userDtos.size(),"size not matched !!");


    }

    }

