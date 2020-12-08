package br.com.devdojo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author William Suane for DevDojo on 6/27/17.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private CustomUserDetailService customUserDetailService;

        @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.authorizeRequests()
        	.anyRequest().authenticated()
            .and()
            .httpBasic()
//      .antMatchers("/*/protected/**").hasRole("USER")
//      .antMatchers("/*/admin/**").hasRole("ADMIN")   
            .and()
            .csrf().disable(); //problema de cors
            
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//                .and().csrf().disable()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, SIGN_UP_URL).permitAll()
//                .antMatchers("/*/protected/**").hasRole("USER")
//                .antMatchers("/*/admin/**").hasRole("ADMIN")
//                .and()
//                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
//                .addFilter(new JWTAuthorizationFilter(authenticationManager(), customUserDetailService));
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailService).passwordEncoder(new BCryptPasswordEncoder());
//    }
        
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	
        auth.inMemoryAuthentication()
                .withUser("abc").password(passwordEncoder.encode("devdojo")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("devdojo")).roles("USER", "ADMIN");

    }
}