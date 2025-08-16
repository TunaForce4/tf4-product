package com.tunaforce.product.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.tunaforce.product.repository.feign")
public class FeignConfig {
}
