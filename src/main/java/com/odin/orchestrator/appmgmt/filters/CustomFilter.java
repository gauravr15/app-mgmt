package com.odin.orchestrator.appmgmt.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.odin.orchestrator.appmgmt.utility.AESHelper;
import com.odin.orchestrator.appmgmt.utility.Utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
@Order(1)
public class CustomFilter extends OncePerRequestFilter {

    @Value("${validate.checksum}")
    private boolean validateChecksum;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${encryption.secretKey}")
    private String aesSecretKey;

    @Value("${encryption.salt}")
    private String aesSalt;

    @Value("${encryption.keySize}")
    private String aesKeySize;

    @Value("${encryption.iterationCount}")
    private String aesIterationCount;

    @Value("${filter.excludedUrls}")
    private String excludedUrls;
    
    @Autowired
    private Utility utility;

    @Autowired
    private AESHelper aesHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Create a RequestMatcher for the excluded URLs
        RequestMatcher requestMatcher = new AntPathRequestMatcher(excludedUrls);

        // Check if the request matches the excluded URLs
        if (requestMatcher.matches(request)) {
            // If the request matches, bypass the filter logic
            filterChain.doFilter(request, response);
            return;
        }

        // Your existing filter logic goes here...

        // Get the JWT token from the request header
        String jwtToken = utility.getToken(request);

        if (StringUtils.hasText(jwtToken) && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);

            // Verify and decode JWT token
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                        .build()
                        .parseClaimsJws(jwtToken)
                        .getBody();

                // Extract user ID from the JWT claims
                String userId = claims.getSubject();

                // Add the userId to the response header
                response.addHeader("userId", userId);

                // Check if the request contains a checksum header
                String checksum = request.getHeader("checksum");

                if (validateChecksum && StringUtils.hasText(checksum)) {
                    // Validate the checksum
                    if (validateChecksum(request, checksum)) {
                        // Decrypt the request body
                        String encryptedRequest = request.getParameter("request");
                        if (StringUtils.hasText(encryptedRequest)) {
                            try {
                                String decryptedRequest = aesHelper.decrypt(encryptedRequest);
                                request.setAttribute("decryptedRequest", decryptedRequest);
                            } catch (Exception e) {
                                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request body");
                                return;
                            }
                        }
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid checksum");
                        return;
                    }
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateChecksum(HttpServletRequest request, String checksum) {
        // Implement checksum validation logic here
        // You can compare the provided checksum with a calculated checksum from the request body
        // Return true if the checksum is valid, false otherwise
        return true; // Replace with your implementation
    }
}
