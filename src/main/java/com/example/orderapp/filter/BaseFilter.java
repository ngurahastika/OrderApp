package com.example.orderapp.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.orderapp.constants.HeaderConstant;
import com.example.orderapp.constants.LoggerConstant;
import com.example.orderapp.utils.CommonUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaseFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String ipAddress = request.getHeader(HeaderConstant.HEADER_X_FORWARDED_FOR);

		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}

		log.info(ipAddress + " - " + request.getMethod() + " - " + request.getRequestURI());

		String requestId = CommonUtils.generateId();
		MDC.put(LoggerConstant.REQ_ID, requestId);

		response.setHeader("X-SESSION-REQ-ID", requestId);
		response.setHeader("X-Permitted-Cross-Domain-Policies", "none");
		response.setHeader("Referrer-Policy", "same-origin");
		response.setHeader("Expect-CT", "max-age=86400");
		response.setHeader("Permissions-Policy",
				"geolocation=(), accelerometer=(), ambient-light-sensor=(), autoplay=(), battery=(), camera=(), display-capture=(), document-domain=(), encrypted-media=(), execution-while-not-rendered=(), execution-while-out-of-viewport=(), fullscreen=(), gyroscope=(), web-share=(), xr-spatial-tracking=(), speaker=(), serial=(), screen-wake-lock=(), publickey-credentials-get=(), payment=()");
		response.setHeader("Cross-Origin-Opener-Policy", "same-origin");
		response.setHeader("Cross-Origin-Embedder-Policy", "require-corp");
		response.setHeader("Cross-Origin-Resource-Policy", "same-origin");
		response.setHeader("Content-Security-Policy", "frame-ancestors 'self'");

		try {
			filterChain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html");
	}

}
