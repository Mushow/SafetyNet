package uk.mushow.safetynet.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Order(1)
@Log4j2
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        log.info("Incoming Request: {} {} - Parameters: {}", requestWrapper.getMethod(), requestWrapper.getRequestURI(), getRequestParameters(requestWrapper));

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);

            if ("POST".equals(requestWrapper.getMethod()) || "PUT".equals(requestWrapper.getMethod())) {
                byte[] requestBody = requestWrapper.getContentAsByteArray();
                if (requestBody.length > 0) {
                    log.info("Request Body: {}", new String(requestBody, StandardCharsets.UTF_8));
                }
            }
        } finally {
            byte[] responseBody = responseWrapper.getContentAsByteArray();
            if (responseBody.length > 0) {
                log.info("Response Body: {}", new String(responseBody, StandardCharsets.UTF_8));
            }

            responseWrapper.copyBodyToResponse();
        }

        log.info("Outgoing Response: {}", responseWrapper.getStatus());
    }

    private Map<String, String[]> getRequestParameters(ContentCachingRequestWrapper requestWrapper) {
        requestWrapper.getParameterMap();
        return requestWrapper.getParameterMap();
    }
}
