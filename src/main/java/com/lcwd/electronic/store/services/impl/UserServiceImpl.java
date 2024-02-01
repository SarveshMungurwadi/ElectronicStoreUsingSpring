package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;
import lombok.extern.flogger.Flogger;
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

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public UserDto createUser(UserDto userDto) {

        String userId=UUID.randomUUID().toString();
        userDto.setUserId(userId);
        //dto->entity
        User user=dtoToEntity(userDto);
        User saveduser=userRepository.save(user);
        //entity->dto
        UserDto newDto=entityToDto(saveduser);
        return newDto;
    }



    @Override
    public UserDto updatedUser(UserDto userDto, String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));
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
    public void deleteUser(String userId) throws IOException {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        String fullPath=imagePath+user.getImageName();
        try{
            Path path= Paths.get(fullPath);
            Files.delete(path);

        }catch (NoSuchFileException e){
            logger.info("User image not found in folder");
            e.printStackTrace();

        }

        userRepository.delete(user);

    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort=Sort.by(sortBy);
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
       Page<User> page= userRepository.findAll(pageable);

        PageableResponse<UserDto> response=Helper.getPageableResponse(page,UserDto.class);
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user not found"));
        UserDto dtoUser=entityToDto(user);
        return dtoUser;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user not found"));
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
//        User user=User.builder().
//                userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName()).build();

        return mapper.map(userDto,User.class);
    }

    private UserDto entityToDto(User saveduser) {
//        UserDto userDto=UserDto.builder()
//                .userId(saveduser.getUserId())
//                .name(saveduser.getName())
//                .email(saveduser.getEmail())
//                .password(saveduser.getPassword())
//                .about(saveduser.getAbout())
//                .gender(saveduser.getGender())
//                .imageName(saveduser.getImageName()).build();

        return mapper.map(saveduser,UserDto.class);
    }
}
