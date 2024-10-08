package com.eventostec.api.domain.coupoun;

import java.util.Date;
import java.util.UUID;

public record CouponRequestDTO(String code, Integer discount,Integer date) {
}
