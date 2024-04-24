package com.example.myproject.security;


import com.example.myproject.security.beforeSecurity.AuthenticationFailureHandler;
import com.example.myproject.security.beforeSecurity.AuthenticationSuccessHandler;
import com.example.myproject.security.beforeSecurity.BeforeAuthenticationFilter;
import com.example.myproject.security.oauth2.CustomOAuth2UserService;
import com.example.myproject.security.oauth2.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;



@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final   CustomOAuth2UserService oAuth2UserService;
        private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
        private final   BeforeAuthenticationFilter beforeAuthenticationFilter;
        private final UserLogoutSuccessHandler userLogoutSuccessHandler;

        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
            auth.setUserDetailsService(userDetailsService());
            auth.setPasswordEncoder(passwordEncoder());
            return auth;
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authenticationProvider());

        }



        @Override
        public void configure(HttpSecurity http) throws Exception {;
            http.authorizeRequests()
                    .antMatchers("/delete/**").hasAuthority("ADMIN")
                    .antMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
                    .antMatchers("/addProduct").hasAnyAuthority("ADMIN", "EDITOR", "SELLER")
                    .anyRequest().permitAll()
                    .and()
                    .addFilterBefore(beforeAuthenticationFilter, BeforeAuthenticationFilter.class)
                    .formLogin().loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler)
                    .permitAll()
                    .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(oAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessHandler)
                    .and()
                    .logout()
                    .logoutSuccessHandler(userLogoutSuccessHandler)
                    .permitAll()
                    .and().exceptionHandling()
                    .accessDeniedPage("/error/403")
                    .and()
                    .rememberMe()
                    .rememberMeServices(rememberMeServices())
                    .tokenValiditySeconds(60*60*24*2)
                    .and().csrf().disable();
                   // غیرفعال کردن مدیریت جلسه


        }

        @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
        private final   AuthenticationSuccessHandler loginSuccessHandler;

        private final AuthenticationFailureHandler loginFailureHandler;

        @Bean
        public RememberMeServices rememberMeServices() throws Exception {
            RememberMeAuthenticationFilter rememberMeServices = new RememberMeAuthenticationFilter(authenticationManager()
                    ,new TokenBasedRememberMeServices("remember-me-key", userDetailsService()));
            rememberMeServices.setAuthenticationSuccessHandler(oAuth2LoginSuccessHandler);
            return rememberMeServices.getRememberMeServices();
            // rememberMeServices.setParameter("myParameter");
            // return rememberMeServices;
        }



    }
