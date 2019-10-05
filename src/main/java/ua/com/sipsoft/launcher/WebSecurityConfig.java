package ua.com.sipsoft.launcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;
import ua.com.sipsoft.utils.AppURL;

/**
 * The Class WebSecurityConfig.
 *
 * @author Pavlo Degtyaryev
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** The user details service. */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Authentication provider.
     *
     * @return the authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
	log.info("Create AuthenticationProvider");
	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	provider.setUserDetailsService(userDetailsService);
	provider.setPasswordEncoder(passwordEncoder());
	return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder(10);
    }

    /**
     * Configure.
     *
     * @param http the http
     * @throws Exception the exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
	log.info("Configure HttpSecurity");
	// @formatter:off

	http
		.csrf().disable() // CSRF handled by Vaadin
		.exceptionHandling().accessDeniedPage("/" + AppURL.ACCESS_DENIED_URL)
		.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/" + AppURL.LOGIN_URL))
		.and()
		.authorizeRequests()
		// allow Vaadin URLs without authentication
		.regexMatchers(
			// Vaadin Flow static resources
			"/VAADIN/.*",
			"/HEARTBEAT/.*",
			"/UIDL/.*",
			"/resources/.*",
			"/PUSH/.*",

			"/" + AppURL.LOGIN_URL + ".*",
			"/" + AppURL.ACCESS_DENIED_URL + ".*",
			"/" + AppURL.LOGIN_REGISTRATION + ".*",
			"/" + AppURL.REGISTRATION_CONFIRM + ".*",
			"/" + AppURL.REGISTRATION_FORGOT + ".*",

			// the standard favicon URI
			"/favicon.ico",

			"/images/.*",

			// the robots exclusion standard
			"/robots.txt",

			// web application manifest
			"/manifest.webmanifest",
			"/sw.js",
			"/offline-page.html",

			// (production mode) static resources //
			"/frontend-es5/.*",
			"/frontend-es6/.*",

			// (development mode) static resources
			"/frontend/.*",

			// (development mode) webjars
			"/webjars/.*"

		)
		.permitAll()
		.regexMatchers(HttpMethod.POST, "/\\?v-r=.*").permitAll()
		// deny other URLs until authenticated
		.antMatchers("/**").fullyAuthenticated()
		.and()
		.formLogin()
		.loginPage("/" + AppURL.LOGIN_URL)
		.defaultSuccessUrl("/" + AppURL.HOME_URL, true)
		.and()
		.logout()
		.logoutUrl("/" + AppURL.LOGOUT_URL)
		.logoutSuccessUrl("/" + AppURL.LOGIN_URL).permitAll();
    }

}
