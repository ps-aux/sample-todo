package sk.luce.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import sk.luce.data.UserAccount;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {


    /**
     * Add security contex to SpEL general context
     * (so we can use it in repos)
     */
    @Bean
    EvaluationContextExtension securityExtension() {
        return new SecurityEvalContextExtension();
    }

    @Configuration
    @Order(2)
    static class BrowserConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private DbUserDetailService userDetailService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll().and().csrf().disable();


            http.headers().frameOptions().disable();

        }

        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
            // Normal authorization manager which encryps the plaintext password
            auth.userDetailsService(userDetailService)
                    .passwordEncoder(UserAccount.PASSWORD_ENCODER);
        }


        @Override
        public void configure(WebSecurity web) throws Exception {
            // Demonstration / dev purposes. No need for admin account.
            web.ignoring().antMatchers("/h2/**");
        }


    }

    @Configuration
    @Order(1)
    static class ApiConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        private DbUserDetailService userDetailService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .anyRequest()
                    // We need hasRole/hasAuthority config for some reason  :|
                    // also mentioned in official documentation
                    // http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#multiple-httpsecurity
                    // We use hasAuthority so ROLE_ is not prepended
                    .hasAuthority("USER")
                    .and().httpBasic()
                    .and().csrf().disable();

        }

        @Autowired
        public void configureAuthentication(AuthenticationManagerBuilder auth)
                throws Exception {
            // Authorization manager using user detail service without encrypting the password
            auth.userDetailsService(userDetailService);
            /** Using the plain text password will also work for api
             * Because of the first one authentication manager
             */
        }


    }
}

