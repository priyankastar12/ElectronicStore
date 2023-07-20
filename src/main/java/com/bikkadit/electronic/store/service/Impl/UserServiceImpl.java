package com.bikkadit.electronic.store.service.Impl;

import com.bikkadit.electronic.store.dto.PageableResponse;
import com.bikkadit.electronic.store.exception.ResourceNotFoundException;
import com.bikkadit.electronic.store.helper.AppConstant;
import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.entity.User;
import com.bikkadit.electronic.store.helper.Helper;
import com.bikkadit.electronic.store.repository.UserRepository;
import com.bikkadit.electronic.store.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${user.profile.image.path}")
    private String imagePath;

    @Override
    public UserDto createUser(UserDto userDto) {
     logger.info("initiating the service call create User data");
        // generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        // dto -> entity
        User user = mapper.map(userDto,User.class);
        User saveUser = userRepository.save(user);

        // entity -> dto
        UserDto newDto = mapper.map(user,UserDto.class);
        logger.info("Complete the service call create User data");

        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        logger.info("initiating the service call update User data with userId :{}",userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        user.setName(userDto.getName());
        // email update
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());

        user.setImageName(userDto.getImageName());
        // save data
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = mapper.map(user,UserDto.class);

        logger.info("complete the service call update User data with userId :{}",userId);


        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        logger.info("initiating the service call delete User data with userId :{}",userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));

        // delete user profile image
        // images/user/abc.png
       String fullPath = imagePath + user.getImageName();
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.info("User image not found in folder !!");
            ex.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        // delete user

        userRepository.delete(user);

        logger.info("complete the service call delete User data with userId :{}",userId);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize , String sortBy, String sortDir) {
        logger.info("initiating the service call getAllUser data  ");

       Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) :(Sort.by(sortBy).ascending()) ;


        // PageNumber default starts from 0
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);

        PageableResponse<UserDto> response = Helper.getPageableResponse(page,UserDto.class);
        logger.info("complete the service call getAllUser data  ");

        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        logger.info("initiating the service call getUserById with userId :{}  " ,userId);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        logger.info("Complete the service call getUserById with userId :{}  " ,userId);

        return mapper.map(user,UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        logger.info("initiating the service call getUserByEmail with email :{}  " ,email);


        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND));
        logger.info("Complete the service call getUserByEmail with email :{}  " ,email);

        return mapper.map(user,UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        logger.info("initiating the service call searchUser with keyword :{}  " ,keyword);

        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> mapper.map(user,UserDto.class)).collect(Collectors.toList());

        logger.info("complete the service call searchUser with email :{}  " ,keyword);
        return dtoList;
    }

    //   dto to entity
    private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();
        return mapper.map(userDto, User.class);
    }

    // entity to dto
    private UserDto entityToDto(User saveUser) {
        UserDto userDto = UserDto.builder()
                .userId(saveUser.getUserId())
                .name(saveUser.getName())
                .email(saveUser.getEmail())
                .password(saveUser.getPassword())
                .about(saveUser.getAbout())
                .gender(saveUser.getGender())
                .imageName(saveUser.getImageName())
                .build();
        return mapper.map(saveUser,UserDto.class);
    }}



