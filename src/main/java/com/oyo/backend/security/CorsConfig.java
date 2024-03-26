package com.oyo.backend.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

@Configuration
@EnableWebMvc
public class CorsConfig {
    private static final Long MAX_AGE = 3600L;
    private static final int CORS_FILTER_ORDER = -120;

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowCredentials(true); // to allow browser to send credentials along with request
        // like JWT, cookies, session Id's
        config.addAllowedOrigin("http://localhost:5173");


        config.setAllowedHeaders(Arrays.asList(// For preFlight request.
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT));
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()));

        config.setMaxAge(MAX_AGE); // How long the preflight request can be cached in the browser's memory :

        source.registerCorsConfiguration("/**", config);// configuring all end points with this CORS object.

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source)); // adding CORS filter into the filter chain :

        bean.setOrder(CORS_FILTER_ORDER); // Setting the order of execution of CORS filter in filter chain :

        return bean;
    }
}