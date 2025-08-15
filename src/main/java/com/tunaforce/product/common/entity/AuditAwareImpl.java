package com.tunaforce.product.common.entity;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // TODO createdBy, updatedBy를 위한 로그인한 유저 id 받아오기

        return Optional.ofNullable(null);
    }
}
