package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDto createUser(UserDto userDto) {

        String userId=UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //dto->entity
        User user=dtoToEntity(userDto);
        User saveduser=userRepository.save(user);
        //entity->dto
        UserDto newDto=entityToDto(saveduser);
        return null;
    }



    @Override
    public UserDto updatedUser(UserDto userDto, String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setImageName(userDto.getImageName());
        user.setPassword(userDto.getPassword());
        User updatedUser=userRepository.save(user);
        UserDto updatedDto=entityToDto(updatedUser);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        userRepository.delete(user);

    }

    @Override
    public List<UserDto> getAllUser() {
       List<User> users= userRepository.findAll();
       List<UserDto> dtoList=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
        UserDto dtoUser=entityToDto(user);
        return dtoUser;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found"));
        UserDto dtoUser=entityToDto(user);
        return dtoUser;

    }

    @Override
    public List<UserDto> searchUser(String keyWord) {
        List<User> users=userRepository.findByNameContaining(keyWord);
        List<UserDto> dtoList=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
        return dtoList;
    }

    private User dtoToEntity(UserDto userDto) {
        User user=User.builder().
                userId(userDto.getUserId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .about(userDto.getAbout())
                .gender(userDto.getGender())
                .imageName(userDto.getImageName()).build();

        return user;
    }

    private UserDto entityToDto(User saveduser) {
        UserDto userDto=UserDto.builder()
                .userId(saveduser.getUserId())
                .name(saveduser.getName())
                .email(saveduser.getEmail())
                .password(saveduser.getPassword())
                .about(saveduser.getAbout())
                .gender(saveduser.getGender())
                .imageName(saveduser.getImageName()).build();

        return userDto;
    }
}
