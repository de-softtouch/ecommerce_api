package com.api.ecommerceweb.security;

import com.api.ecommerceweb.enumm.ERole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Enable CORS and disable CSRF
        http.cors().and().csrf().disable();
        // Disable session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // Set unauthorized requests exception handler
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) ->
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()))
                .and();
        // Set permissions on endpoints
        http.authorizeRequests()
                //  public endpoints
                .antMatchers("/api/v1/public/**").permitAll()
                .antMatchers("/api/v1/member/**").hasAnyAuthority(ERole.ROLE_MEMBER.name(), ERole.ROLE_SELLER.name())
                .antMatchers("/api/v1/seller/**").hasAuthority(ERole.ROLE_SELLER.name())
                .antMatchers("/api/v1/admin/**").hasAuthority(ERole.ROLE_ADMIN.name())
                //  private endpoints
                .anyRequest().authenticated();
        // Add JWT token filter
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


    }

    @Configuration
    public class CorsConfig {

        @Bean
        public CorsFilter corsFilter() {
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowCredentials(true);
            config.addAllowedOrigin("http://localhost:3000");
            config.addAllowedHeader("*");
            config.addAllowedMethod("POST");
            config.addAllowedMethod("PUT");
            config.addAllowedMethod("GET");
            config.addAllowedMethod("PATCH");
            config.addAllowedMethod("DELETE");
            source.registerCorsConfiguration("/**", config);
            return new CorsFilter(source);
        }
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SecurityContextHolderAwareRequestFilter securityContextHolderAwareRequestFilter() {
        return new SecurityContextHolderAwareRequestFilter();
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper model = new ModelMapper();
        model.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return model;
    }


}
