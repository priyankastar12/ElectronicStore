package com.bikkadit.electronic.store.controller;


import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.entity.User;
import com.bikkadit.electronic.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Arrays;
import java.util.List;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {


    private User user;
    @MockBean
    private UserService userService;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void init(){

        user= User.builder()
                .name("Priyanka")
                .email("priyanka@gmail.com")
                .about("this is testing create method")
                .gender("female")
                .imageName("abc.png")
                .password("andg")
                .build();


    }
        // create User Test
    @Test
    public void createUserTest() throws Exception {
        UserDto dto = mapper.map(user, UserDto.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        // actual request for url
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/users")

                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON)
                )
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name").exists());

    }
    // Update User Test
    @Test
   public void updateUserTest() throws Exception {
       String userId="123";
        UserDto dto= this.mapper.map(user,UserDto.class);
      Mockito.when(userService.updateUser(Mockito.any(),Mockito.anyString())).thenReturn(dto);
       this.mockMvc.perform(
               MockMvcRequestBuilders.put("/users/userId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON)
        )
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").exists());
    }
    private String convertObjectToJsonString(Object user){
        try{


            return new ObjectMapper().writeValueAsString(user);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    // get All users test
    @Test
    public void getAllUsersTest() {

    UserDto object1 =  UserDto.builder().name("Priyanka").email("priyanka@gmail.com").password("priyanka").about("Testing").build();
    UserDto object2 =  UserDto.builder().name("Shital").email("priyanka@gmail.com").password("priyanka").about("Testing").build();
    UserDto object3 =  UserDto.builder().name("Pradnya").email("priyanka@gmail.com").password("priyanka").about("Testing").build();
    UserDto object4 =  UserDto.builder().name("Pravin").email("priyanka@gmail.com").password("priyanka").about("Testing").build();


        PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(object1,object2,object3,object4));
        pageableResponse.setContent(Arrays.asList());
        pageableResponse.setLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setTotalPages(100);
        pageableResponse.setTotalElements(1000);

        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(pageableResponse);

    }

    // delete user
    @Test
    public void deleteUserTest() throws Exception {
        String userId = "userTest123";
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/"+userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
    // get user By id

    @Test
    public void getUserTest() throws Exception {
        String userId = "getUserTest";
        UserDto object = UserDto.builder().name("Priyanaka").email("priyanaka@gmail.com").password("priyanka123").about("Test").build();
        Mockito.when(userService.getUserById(Mockito.any())).thenReturn(object);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/"+userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());


    }
             // serach user
         @Test
         public void searchUserTest() throws Exception {
        UserDto object1=UserDto.builder().name("Priyanka").email("priyanka@gmail.com").password("fh415").about("Test").build();
        UserDto object2=UserDto.builder().name("Shital").email("shital@gmail.com").password("fh415").about("Test").build();
        UserDto object3=UserDto.builder().name("Ankita").email("ankita@gmail.com").password("fh415").about("Test").build();
        UserDto object4=UserDto.builder().name("Ashvini").email("ashv@gmail.com").password("fh415").about("Test").build();

                   String keyword="p";
             List<UserDto>  userDtos=Arrays.asList(object1,object2,object3,object4)  ;
             Mockito.when(userService.searchUser(Mockito.anyString())).thenReturn(userDtos);
             mockMvc.perform(MockMvcRequestBuilders.get("/users/serach/"+keyword)
                     .contentType(MediaType.APPLICATION_JSON)
                     .accept(MediaType.APPLICATION_JSON))
                     .andDo(print())
                     .andExpect(status().isOk());

         }

}
