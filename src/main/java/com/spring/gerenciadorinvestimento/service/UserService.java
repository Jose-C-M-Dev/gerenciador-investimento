package com.spring.gerenciadorinvestimento.service;

import com.spring.gerenciadorinvestimento.controller.dto.AccountResponseDto;
import com.spring.gerenciadorinvestimento.controller.dto.CreateAccountDto;
import com.spring.gerenciadorinvestimento.controller.dto.CreateUserDto;
import com.spring.gerenciadorinvestimento.controller.dto.UpdateUserDto;
import com.spring.gerenciadorinvestimento.entity.Account;
import com.spring.gerenciadorinvestimento.entity.BillingAddress;
import com.spring.gerenciadorinvestimento.entity.User;
import com.spring.gerenciadorinvestimento.repository.AccountRepository;
import com.spring.gerenciadorinvestimento.repository.BillingAddressRepository;
import com.spring.gerenciadorinvestimento.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    @Transactional
    public UUID createUser(CreateUserDto createUserDto) {

        var entity = new User(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password());

        var userSaved = userRepository.save(entity);

        return userSaved.getUserId();
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> listAllUsers(){
        return userRepository.findAll();
    }

    public void updateUserById(String userId, UpdateUserDto updateUserDto){
        var id = UUID.fromString(userId);
        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()){
            var user = userEntity.get();
            if(updateUserDto.username() != null){
                user.setUsername(updateUserDto.username());
            }
            if(updateUserDto.password() != null){
                user.setPassword(updateUserDto.password());
            }
            userRepository.save(user);
        }

    }

    public void deleteUserById(String userId){
        var id = UUID.fromString(userId);

        var userExists = userRepository.existsById(id);

        if(userExists){
            userRepository.deleteById(id);
        }
    }

    @Transactional
    public void createAccount(String userId, CreateAccountDto createAccountDto) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não existe"));

        if (isNull(user.getAccounts())) {
            user.setAccounts(new ArrayList<>());
        }

        var account = new Account();
        account.setUser(user);
        account.setDescription(createAccountDto.description());

        var billingAddress = new BillingAddress();
        billingAddress.setAccount(account);
        billingAddress.setStreet(createAccountDto.street());
        billingAddress.setNumber(createAccountDto.number());

        account.setBillingAddress(billingAddress);

        user.getAccounts().add(account);

        accountRepository.save(account);
    }

    public List<AccountResponseDto> listAccounts(String userId) {
        var user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        return user.getAccounts().stream().map(account ->
                new AccountResponseDto(account.getAccountId().toString(),account.getDescription())).toList();
    }
}
