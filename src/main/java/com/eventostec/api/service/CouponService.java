package com.eventostec.api.service;

import com.eventostec.api.domain.coupoun.Coupon;
import com.eventostec.api.domain.coupoun.CouponRequestDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.CuponRepository;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CuponRepository cuponRepository;

    @Autowired
    private EventRepository eventRepository;

    public Coupon createCupon(CouponRequestDTO coupon){

        Event event = eventRepository.findById(coupon.id()).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Coupon newCoupon = new Coupon();
        newCoupon.setDate(new Date(coupon.date()));
        newCoupon.setCode(coupon.code());
        newCoupon.setDiscount(coupon.discount());
        newCoupon.setEvent(event);

        return cuponRepository.save(newCoupon);
    }

}
