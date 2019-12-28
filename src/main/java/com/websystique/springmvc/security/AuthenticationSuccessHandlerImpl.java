package com.websystique.springmvc.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException {


        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (authorities.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin/list");
        } else if (authorities.contains("ROLE_USER")) {
            response.sendRedirect("/user");
        } else {
            throw new IllegalStateException("!!!!!!!------>>>>the user mast be USE or ADMIN !!!!!!!!!");
        }
    }

}
