package com.eventostec.api.controller;

import com.eventostec.api.domain.coupoun.Coupon;
import com.eventostec.api.domain.coupoun.CouponRequestDTO;
import com.eventostec.api.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("/create")
    public ResponseEntity<Coupon> createCupon(@RequestBody CouponRequestDTO data){
        Coupon coupon = couponService.createCupon(data);
        return ResponseEntity.ok(coupon);
    }

}
