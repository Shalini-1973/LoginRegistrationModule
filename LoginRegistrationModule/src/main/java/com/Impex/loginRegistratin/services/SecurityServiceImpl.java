package com.Impex.loginRegistratin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	UserDetailsService userDeatailService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Override
	public boolean login(String username, String password) {
		
		UserDetails userDetail = userDeatailService.loadUserByUsername(username);
		
		UsernamePasswordAuthenticationToken token=new UsernamePasswordAuthenticationToken(userDetail,password,userDetail.getAuthorities());
		
		authenticationManager.authenticate(token);
		
		boolean result = token.isAuthenticated();
		if(result) {
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		
		return result;
	}

}
