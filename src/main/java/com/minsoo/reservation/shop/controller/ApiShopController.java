package com.minsoo.reservation.shop.controller;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.minsoo.reservation.common.exception.ShopNotFoundException;
import com.minsoo.reservation.common.model.ResponseError;
import com.minsoo.reservation.common.model.ResponseResult;
import com.minsoo.reservation.common.model.ServiceResult;
import com.minsoo.reservation.shop.domain.Shop;
import com.minsoo.reservation.shop.model.ShopInput;
import com.minsoo.reservation.shop.model.ShopSearchInput;
import com.minsoo.reservation.shop.model.ShopSearchOutput;
import com.minsoo.reservation.shop.repository.ShopRepository;
import com.minsoo.reservation.shop.service.ShopService;
import com.minsoo.reservation.util.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiShopController {
    private final ShopRepository shopRepository;
    private final ShopService shopService;

    @PostMapping("/api/shop")
    public ResponseEntity<?> add(@RequestHeader("TOKEN") String token,
                                 @RequestBody @Valid ShopInput shopInput
            , Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        String email;
        try {
            email = JWTUtils.getIssuer(token);
        } catch (SignatureVerificationException e) {
            return new ResponseEntity<>("토큰 정보가 정확하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = shopService.add(email, shopInput);
        return ResponseResult.result(result);
    }

    @PostMapping("/api/shop/detail")
    public ResponseEntity<?> search(@RequestBody @Valid ShopSearchInput shopSearchInput,
                                    Errors errors) {
        List<ResponseError> responseErrorList = new ArrayList<>();
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> responseErrorList.add(ResponseError.of((FieldError) e)));
            return new ResponseEntity<>(responseErrorList, HttpStatus.BAD_REQUEST);
        }
        Shop shop = shopRepository.findByShopName(shopSearchInput.getShopName())
                .orElseThrow(() -> new ShopNotFoundException("상점이 존재하지 않습니다"));
        return ResponseResult.success(ShopSearchOutput.of(shop));
    }

    @GetMapping("/api/reservation/approve/{id}")
    public ResponseEntity<?> approve(@PathVariable Long id) {
        ServiceResult result = shopService.approve(id);
        return ResponseResult.result(result);
    }

    @GetMapping("/api/reservation/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        ServiceResult result = shopService.reject(id);
        return ResponseResult.result(result);
    }
}
