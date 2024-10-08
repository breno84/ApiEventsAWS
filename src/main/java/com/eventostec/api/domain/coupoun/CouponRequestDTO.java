package com.eventostec.api.domain.coupoun;

import java.util.UUID;

public record CouponRequestDTO(UUID id, String code, Integer discount, Long date) {
}
