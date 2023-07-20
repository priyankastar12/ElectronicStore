package com.bikkadit.electronic.store.controller;

import com.bikkadit.electronic.store.dto.ImageResponse;
import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.helper.AppConstant;
import com.bikkadit.electronic.store.dto.ApiResponseMessage;
import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.service.FileService;
import com.bikkadit.electronic.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imageUploadPath;


    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    // Create

    /**
     * @author Priyanka_Pawar
     * @param userDto
     * @return
     */
          @PostMapping
    public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto)
    {

        logger.info("Initilizing createUser method");
        UserDto userDto1 = userService.createUser(userDto);
        logger.info("Execution completed of method createUser");
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }
    // Update

    /**
     * @author Priyanka_Pawar
     * @param userId
     * @param userDto
     * @return
     */
        @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(
              @PathVariable("userId") String userId,
              @Valid
              @RequestBody UserDto userDto)
        {
            logger.info("Initilizing updateUser method for id:"+userId);
            UserDto updateUserDto = userService.updateUser(userDto,userId);
            logger.info("Execution completed of method updateUser for id:"+userId);
            return new ResponseEntity<>(updateUserDto,HttpStatus.OK);

        }// Delete

    /**
     * @author Priyanka_Pawar
     * @param userId
     * @return
     */
         @DeleteMapping("/{userId}")
        public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId)
         {
             logger.info("Initilizing deleteUser method for id:"+userId);
             userService.deleteUser(userId);
             ApiResponseMessage message = ApiResponseMessage
                     .builder()
                     .message(AppConstant.USER_DELETED)
                     .success(true)
                     .status(HttpStatus.OK)
                     .build();
             logger.info("Execution completed of method deleteUser for id:"+userId);
             return new ResponseEntity<>(message,HttpStatus.OK);


         }

         //Get all

    /**
     * @author Priyanka_Pawar
     * @return
     */
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam( value="pageNumber",defaultValue = "0",required = false) int pageNumber,

            @RequestParam(  value = "pageSize",defaultValue = "10" ,required = false) int pageSize,
            @RequestParam(  value = "sortBy",defaultValue = "name" ,required = false) String sortBy,
            @RequestParam(  value = "sortDir",defaultValue = "asc" ,required = false)  String sortDir


    ) {



            logger.info("Initilizing getAllUsers method");
            return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir), HttpStatus.OK);

        }
        // Get single

    /**
     * @author Priyanka_Pawar
     * @param userId
     * @return
     */
        @GetMapping("/{userId}")
        public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
            logger.info("Initilizing getUserById method");
            return new ResponseEntity<UserDto>(userService.getUserById(userId),HttpStatus.OK);



        }
        // get by email

    /**
     * @author Priyanka_Pawar
     * @param email
     * @return
     */
        @GetMapping("/email/{email}")
        public ResponseEntity<UserDto> getUserByEmail (@PathVariable String email){
            logger.info("Initilizing getUserByEmail method");
            return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
        }



        // Search

    /**
     * @author Priyanka_Pawar
     * @param keywords
     * @return
     */
        @GetMapping("/search/{keywords}")
        public ResponseEntity<List<UserDto>> searchUser (@PathVariable String keywords)
        {
            logger.info("Initilizing searchUser method");
            return new ResponseEntity<List<UserDto>>(userService.searchUser(keywords), HttpStatus.OK);

        }

       // upload user image

    /**
     * @author Priyanka_Pawar
     * @apiNote Upload User Image
     * @param image
     * @param userId
     * @return
     * @throws IOException
     */
        @PostMapping("/image/{userId}")
        public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image,@PathVariable String userId) throws IOException {

        String imageName= fileService.uploadFile(image,imageUploadPath);

         UserDto user = userService.getUserById(userId);
         user.setImageName(imageName);
         UserDto userDto =  userService.updateUser(user,userId);
        ImageResponse imageResponse= ImageResponse.builder().imageName(imageName).message("user image uploaded sucessfully !!").success(true).status(HttpStatus.CREATED).build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
        }


      // serve user image

    /**
     * @author Priyanka_Pawar
     * @param userId
     * @param response
     * @throws IOException
     */
        @GetMapping("/image/{userId}")
        public void serveUserImage(@PathVariable String userId,HttpServletResponse response) throws IOException {

        UserDto user=  userService.getUserById(userId);
        logger.info("User image name: {}",user.getImageName());
        InputStream resource =  fileService.getResource(imageUploadPath,user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

            StreamUtils.copy(resource,response.getOutputStream());
        }


    }
