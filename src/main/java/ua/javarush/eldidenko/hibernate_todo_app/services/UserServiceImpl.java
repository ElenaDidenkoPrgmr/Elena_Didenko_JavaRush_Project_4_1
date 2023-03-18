package ua.javarush.eldidenko.hibernate_todo_app.services;

import ua.javarush.eldidenko.hibernate_todo_app.dto.UserDTO;
import ua.javarush.eldidenko.hibernate_todo_app.entites.User;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserNameIsTakenException;
import ua.javarush.eldidenko.hibernate_todo_app.exceptions.UserUnauthorizedException;
import ua.javarush.eldidenko.hibernate_todo_app.mappers.UserMapper;
import ua.javarush.eldidenko.hibernate_todo_app.repositories.UserRepository;
import ua.javarush.eldidenko.hibernate_todo_app.request.UserRequest;
import ua.javarush.eldidenko.hibernate_todo_app.utils.PasswordHash;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userMapper = UserMapper.INCTANCE;
    }

    @Override
    public UserDTO fetchUserById(Long userId) {
        /*return userRepository.fetchById(userId)
                .map(userMapper::userToDTO)
                .orElseThrow(() -> new IllegalArgumentException("User not Found"));*/
        User user = userRepository.fetchById(userId);
        return userMapper.userToDTO(user);
    }

    @Override
    public UserDTO createUser(UserRequest userRequest) throws UserNameIsTakenException {
        //User existedUser = userRepository.fetchByUserName(userRequest.getUsername());
        hashPassword(userRequest);
        if (checkNameIsFree(userRequest.getUsername())) {
            User newUser = userRepository.save(userMapper.requestToUser(userRequest));
            return userMapper.userToDTO(newUser);
        } else throw new UserNameIsTakenException();
    }

      /* @Override
    public UserDTO fetchUserById(Long userId) {
        User user =  userRepository.fetchById(userId);
        return userMapper.userToDTO(user);
    }*/

    @Override
    public UserDTO updateUser(UserRequest userRequest, Long userId) throws UserNameIsTakenException {

        if (checkNameIsFree(userRequest.getUsername(), userId)) {
            if (userRequest.getPassword()!=null){
                hashPassword(userRequest);
            }

            User userBefore = userRepository.fetchById(userId);
            User userModified = userMapper.updateUserFromRequest(userBefore, userRequest);

            User userAfter = userRepository.updateUser(userModified);
            return userMapper.userToDTO(userAfter);
        } else throw new UserNameIsTakenException();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteUser(id);
    }

    private boolean checkNameIsFree(String username) {
        User existedUser = userRepository.fetchByUserName(username);
        return (existedUser == null);
    }

    @Override
    public User fetchByUserName(String username) {
        return userRepository.fetchByUserName(username);
    }

    @Override
    public void hashPassword(UserRequest userRequest) {
        String hashPassword = PasswordHash.hash(userRequest.getPassword().toCharArray());
        userRequest.setPassword(hashPassword);
    }
    private boolean checkNameIsFree(String username, Long challengerId) {
        User existedUser = userRepository.fetchByUserName(username);
        return (existedUser == null || existedUser.getId().equals(challengerId));
    }

    @Override
    public Long authenticateUser(String username, char[] password) throws UserUnauthorizedException {
        User user = fetchByUserName(username);

        if (user == null || badPassword(user, password)){
            throw new UserUnauthorizedException();
        }
        return user.getId();
    }

    private static boolean badPassword(User user, char[] password) {
        return !user.getPassword().equals(PasswordHash.hash(password));
    }
}
