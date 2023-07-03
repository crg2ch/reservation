package com.minsoo.reservation.user.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.minsoo.reservation.common.exception.ExistsEmailException;
import com.minsoo.reservation.common.exception.PasswordNotMatchException;
import com.minsoo.reservation.common.exception.UserNotFoundException;
import com.minsoo.reservation.common.model.ResponseError;
import com.minsoo.reservation.common.model.ResponseResult;
import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.shop.model.ReservationInput;
import com.minsoo.reservation.user.domain.User;
import com.minsoo.reservation.user.model.UserInput;
import com.minsoo.reservation.user.model.UserLogin;
import com.minsoo.reservation.user.repository.UserRepository;
import com.minsoo.reservation.user.service.UserService;
import com.minsoo.reservation.util.JWTUtils;
import com.minsoo.reservation.util.PasswordUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiUserController {
    private final UserRepository userRepository;
    private final UserService userService;

    private static String getEncryptedPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    @PostMapping("/api/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserInput userInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if (userRepository.countByEmail(userInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        String encryptedPassword = getEncryptedPassword(userInput.getPassword());

        userRepository.save(User.builder()
                .email(userInput.getEmail())
                .userName(userInput.getUserName())
                .password(encryptedPassword)
                .phone(userInput.getPhone())
                .build());
        return ResponseResult.success("사용자 회원 가입이 완료되었습니다.");
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<?> deleteUser(@RequestHeader("TOKEN") String token) {
        String userEmail;
        try {
            userEmail = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("사용자의 토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        try {
            userRepository.delete(user);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("제약조건에 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("회원 탈퇴 중에 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return ResponseResult.success("사용자 회원 탈퇴가 정상적으로 되었습니다.");
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid UserLogin userLogin, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new UserNotFoundException("사용자 정보가 없습니다."));
        if (!PasswordUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
        return ResponseEntity.ok().body(JWTUtils.createToken(user));
    }

    @PostMapping("/api/user/reservation")
    public ResponseEntity<?> reserve(@RequestHeader("TOKEN") String token,
                                     @RequestBody @Valid ReservationInput reservationInput
            , Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        String userEmail;
        try {
            userEmail = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("사용자의 토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = userService.reserve(userEmail, reservationInput);
        return ResponseResult.result(result);
    }
}
