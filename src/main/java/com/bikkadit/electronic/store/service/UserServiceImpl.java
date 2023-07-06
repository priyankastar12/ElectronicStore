package com.bikkadit.electronic.store.service;

import com.bikkadit.electronic.store.dto.UserDto;
import com.bikkadit.electronic.store.entity.User;
import com.bikkadit.electronic.store.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        // generate unique id in string format
        String userId = UUID.randomUUID().toString();
        userDto.setUserId(userId);

        // dto -> entity
        User user = dtoToEntity(userDto);
        User saveUser = userRepository.save(user);

        // entity -> dto
        UserDto newDto = entityToDto(saveUser);
        return newDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        user.setName(userDto.getName());
        // email update
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        User user1 = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        user.setName(userDto.getName());
        // emailUpdate
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setImageName(userDto.getImageName());
        // save data
        User updatedUser = userRepository.save(user);
        UserDto updatedDto = entityToDto(updatedUser);


        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id"));
        userRepository.delete(user);


    }

    @Override
    public List<UserDto> getAllUser() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with given id !!"));

        return entityToDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with given id"));

        return entityToDto(user);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());


        return dtoList;
    }

    //   dto to entity
    private User dtoToEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .imageName(userDto.getImageName())
                .build();
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
        return userDto;
    }}



