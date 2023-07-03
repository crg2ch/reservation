package com.minsoo.reservation.shop.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.minsoo.reservation.common.exception.ExistsEmailException;
import com.minsoo.reservation.common.exception.PartnerNotFoundException;
import com.minsoo.reservation.common.exception.PasswordNotMatchException;
import com.minsoo.reservation.common.model.ResponseError;
import com.minsoo.reservation.common.model.ResponseResult;
import com.minsoo.reservation.shop.domain.Partner;
import com.minsoo.reservation.shop.model.PartnerInput;
import com.minsoo.reservation.shop.model.PartnerLogin;
import com.minsoo.reservation.shop.repository.PartnerRepository;
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
public class ApiPartnerController {
    private final PartnerRepository partnerRepository;

    private static String getEncryptedPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    @PostMapping("/api/partner")
    public ResponseEntity<?> addPartner(@RequestBody @Valid PartnerInput partnerInput, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }

        if (partnerRepository.countByEmail(partnerInput.getEmail()) > 0) {
            throw new ExistsEmailException("이미 존재하는 이메일입니다.");
        }

        String encryptedPassword = getEncryptedPassword(partnerInput.getPassword());

        partnerRepository.save(Partner.builder()
                .email(partnerInput.getEmail())
                .partnerName(partnerInput.getPartnerName())
                .password(encryptedPassword)
                .phone(partnerInput.getPhone())
                .build());
        return ResponseResult.success("파트너 회원 가입이 완료되었습니다.");
    }

    @DeleteMapping("/api/partner")
    public ResponseEntity<?> deletePartner(@RequestHeader("TOKEN") String token) {
        String partnerEmail;
        try {
            partnerEmail = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("파트너의 토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
        Partner partner = partnerRepository.findByEmail(partnerEmail)
                .orElseThrow(() -> new PartnerNotFoundException("파트너 정보가 없습니다."));
        try {
            partnerRepository.delete(partner);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("파트너 제약조건에 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("파트너 회원 탈퇴 중에 문제가 발생하였습니다.", HttpStatus.BAD_REQUEST);
        }
        return ResponseResult.success("파트너 회원 탈퇴가 정상적으로 되었습니다.");

    }

    @PostMapping("/api/partner/login")
    public ResponseEntity<?> createToken(@RequestBody @Valid PartnerLogin partnerLogin, Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        Partner partner = partnerRepository.findByEmail(partnerLogin.getEmail())
                .orElseThrow(() -> new PartnerNotFoundException("파트너 정보가 없습니다."));
        if (!PasswordUtils.equalPassword(partnerLogin.getPassword(), partner.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
        return ResponseEntity.ok().body(JWTUtils.createToken(partner));
    }

}
