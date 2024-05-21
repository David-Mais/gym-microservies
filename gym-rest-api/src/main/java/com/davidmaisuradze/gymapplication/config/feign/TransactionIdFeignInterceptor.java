package com.davidmaisuradze.gymapplication.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class TransactionIdFeignInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String transactionId = (String) request.getAttribute("transactionId");
            if (transactionId != null) {
                requestTemplate.header("transactionId", transactionId);
            }

            String token = request.getHeader(AUTHORIZATION_HEADER);
            if (token != null) {
                requestTemplate.header(AUTHORIZATION_HEADER, token);
            }
        } else {
            // Handle case where request attributes are not available (e.g., in async threads)
            String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (token != null) {
                requestTemplate.header(AUTHORIZATION_HEADER, "Bearer " + token);
            }
        }
    }
}
