package com.gavin.myapp.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.gavin.myapp.config.Constants;
import com.gavin.myapp.domain.Authority;
import com.gavin.myapp.domain.User;
import com.gavin.myapp.repository.AuthorityRepository;
import com.gavin.myapp.repository.UserRepository;
import com.gavin.myapp.security.AuthoritiesConstants;
import com.gavin.myapp.security.SecurityUtils;
import com.gavin.myapp.service.dto.AdminUserDTO;
import com.gavin.myapp.service.dto.UserDTO;
import com.gavin.myapp.service.mapper.UserMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service class for managing users.
 */
@Service
public class UserService extends BaseServiceImpl<UserRepository, User> {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final UserMapper userMapper;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthorityRepository authorityRepository,
        CacheManager cacheManager,
        UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.userMapper = userMapper;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(User::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                }
            );
    }

    public User registerUser(AdminUserDTO userDTO, String password) {
        userRepository
            .findOneByLogin(userDTO.getLogin().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(true); // modity by wangxin
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        List<Authority> authorities = new ArrayList<>();
        /* comment by wangxin
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add); */
        authorityRepository.findFirstByCode(AuthoritiesConstants.USER).ifPresent(authorities::add); // add by wangxin
        newUser.setAuthorities(authorities);
        userRepository.insert(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.deleteById(existingUser.getId());
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(AdminUserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setMobile(userDTO.getMobile());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            List<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(
                    authorityDTO -> {
                        Authority authority = new Authority();
                        authority.setId(authorityDTO.getId());
                        authority.setCode(authorityDTO.getCode());
                        return authority;
                    }
                )
                .collect(Collectors.toList());
            user.setAuthorities(authorities);
        }
        userRepository.insert(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update mobile for a specific user, and return the modified user.
     *
     * @param mobile mobile to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUserMobile(String mobile) {
        return Optional
            .of(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {
                    this.clearUserCaches(user);
                    user.setMobile(mobile);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                }
            )
            .map(userMapper::userToAdminUserDTO);
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<AdminUserDTO> updateUser(AdminUserDTO userDTO) {
        User findUser = userRepository.selectById(userDTO.getId());
        if (findUser != null) {
            this.clearUserCaches(findUser);
            findUser.setLogin(userDTO.getLogin().toLowerCase());
            findUser.setFirstName(userDTO.getFirstName());
            findUser.setLastName(userDTO.getLastName());
            if (userDTO.getEmail() != null) {
                findUser.setEmail(userDTO.getEmail().toLowerCase());
            }
            findUser.setImageUrl(userDTO.getImageUrl());
            findUser.setActivated(userDTO.isActivated());
            findUser.setLangKey(userDTO.getLangKey());
            List<Authority> managedAuthorities = findUser.getAuthorities();
            managedAuthorities.clear();
            userDTO
                .getAuthorities()
                .stream()
                .map(authorityDTO -> authorityRepository.findById(authorityDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(managedAuthorities::add);
            this.clearUserCaches(findUser);
            log.debug("Changed Information for User: {}", findUser);
            return Optional.of(userMapper.userToAdminUserDTO(findUser));
        } else {
            return Optional.empty();
        }
    }

    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(
                user -> {
                    userRepository.deleteById(user.getId());
                    this.clearUserCaches(user);
                    log.debug("Deleted User: {}", user);
                }
            );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param langKey   language key.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                }
            );
    }

    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                }
            );
    }

    public IPage<AdminUserDTO> getAllManagedUsers(IPage<User> pageable) {
        return userRepository.selectPage(pageable, null).convert(userMapper::userToAdminUserDTO);
    }

    @Transactional(readOnly = true)
    public IPage<UserDTO> getAllPublicUsers(IPage<User> pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).convert(UserDTO::new);
    }

    public Optional<AdminUserDTO> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login).map(userMapper::userToAdminUserDTO);
    }

    public Optional<UserDTO> findOneByMobile(String mobile) {
        log.debug("Request to get Person : {}", mobile);
        return userRepository.findByMobile(mobile).map(UserDTO::new);
    }

    public Boolean existsByMobileAndLoginNot(String mobile, String login) {
        log.debug("Request to get Person : {}", mobile);
        return userRepository.existsByMobileAndLoginNot(mobile, login);
    }

    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneWithAuthoritiesByLogin)
            .map(
                user -> {
                    Binder.bindRelations(user);
                    return user;
                }
            );
    }

    public Optional<AdminUserDTO> getUserWithAuthorities(Long id) {
        return Optional.ofNullable(userRepository.selectById(id)).map(userMapper::userToAdminUserDTO);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.deleteById(user.getId());
                    this.clearUserCaches(user);
                }
            );
    }

    /**
     * Gets a list of all the authorities.
     * @return a list of all the authorities.
     */
    public List<String> getAuthorities() {
        return authorityRepository.selectList(null).stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null) {
            Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
}
