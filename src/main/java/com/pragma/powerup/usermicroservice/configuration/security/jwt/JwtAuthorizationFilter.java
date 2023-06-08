package com.pragma.powerup.usermicroservice.configuration.security.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private boolean isEndpointMatch(String allowedEndpoint, String actualEndpoint) {
        Pattern pattern = Pattern.compile(allowedEndpoint.replaceAll("\\{id\\}", "[^/]+"));
        return pattern.matcher(actualEndpoint).matches();
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String endpoint = request.getRequestURI();
        Map<String, List<String>> rolesEndpointsMap = new HashMap<>();
        // Agrega más roles y sus respectivos endpoints según sea necesario
        rolesEndpointsMap.put("ROLE_ADMIN", Arrays.asList("/food-court/v1/admin/restaurant"));
        rolesEndpointsMap.put("ROLE_OWNER", Arrays.asList("/food-court/v1/owner/dish", "/food-court/v1/owner/dish/{id}", "/food-court/v1/owner/putDish/{id}", "/food-court/v1/owner/putDishState/{id}", "/food-court/v1/owner/employee"));
        rolesEndpointsMap.put("ROLE_EMPLOYEE", Arrays.asList("/food-court/v1/employee/orders", "/food-court/v1/employee/assign-orders", "/food-court/v1/employee/order-ready", "/food-court/v1/employee/order-delivered"));
        rolesEndpointsMap.put("ROLE_CLIENT", Arrays.asList("/food-court/v1/client/list-restaurants", "/food-court/v1/client/list-dishes-restaurant", "/food-court/v1/client/new-order", "/food-court/v1/client/order-cancel"));


        // Hacer la excepcion para el token
        try {
            String token = getToken(request);
            if (token == null || !JwtUtils.validateJwtToken(token)) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            List<String> roles = JwtUtils.getRoles(token);
             if (!roles.stream().anyMatch(rolesEndpointsMap::containsKey)) {
             response.sendError(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            List<String> allowedEndpoints = new ArrayList<>();
            for (String role : roles) {
                List<String> roleEndpoints = rolesEndpointsMap.get(role);
                if (roleEndpoints != null) {
                    allowedEndpoints.addAll(roleEndpoints);
                }
            }

            boolean isEndpointAllowed = false;
            for (String allowedEndpoint : allowedEndpoints) {
                if (isEndpointMatch(allowedEndpoint, endpoint)) {
                    isEndpointAllowed = true;
                    break;
                }
            }

            if (!isEndpointAllowed) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return;
            }
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

}
