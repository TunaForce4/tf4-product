package com.tunaforce.product.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

public class AuditAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();

        // check type & casting
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String userId = request.getHeader("X-Auth-User-Id");

            return Optional.of(UUID.fromString(userId));
        }

        return Optional.empty();
    }
}
