package kr.co.consulting.MyRESTfulService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {
    @Bean
    UserDetailsService userDetailsService(){

        InMemoryUserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        UserDetails newUser = User.withUsername("user")
                .password(passwordEncoder().encode("passw0rd"))
                .authorities("read")
                .build();

        userDetailsManager.createUser(newUser);
        return userDetailsManager;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //현재 쓰고 있는 API의 시큐리티 중 /h2-console/**" 이 부분은 시큐리티 무시하는 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)-> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**"));

    }

    //강제로 해킹 가능한 상태로 만든다는 느낌
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http,
                                              HandlerMappingIntrospector introspector) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

}

//GET 메소드 등은 서버의 리소스에 변경을 주지 않기 때문에 별도의 작업을 하지 않아도 되지만
//POST, DELETE 등의 메소드는 리소스에 변경을 주기 때문에
//필터체인을 걸어둬야 한다.