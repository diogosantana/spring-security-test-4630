package org.springframework.security.issue4630;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.issue4630.security.RestAuthenticationEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("prod")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	
	@Autowired
	private FixedLocaleFilter fixedLocaleFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf().disable()
			.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationEntryPoint)
				.and()
			.addFilterBefore(fixedLocaleFilter, SecurityContextPersistenceFilter.class)
			.authorizeRequests()
				.antMatchers("/api/**").hasAnyRole("USER")
				.antMatchers(HttpMethod.POST, "/login").permitAll()
				.anyRequest().permitAll()
				.and()
			.formLogin()
				.usernameParameter("email")
				.and()
			.logout()
		;
		// @formatter:on
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		auth
			.inMemoryAuthentication()
			.withUser("user").password("user").roles("USER").and()
			.withUser("staff").password("staff").roles("STAFF").and()
			.withUser("manager").password("manager").roles("STAFF").and()
			.withUser("admin").password("admin").roles("ADMIN");
		// @formatter:on
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

		Map<String, List<String>> roleHierarchyMap = new LinkedHashMap<>();
		roleHierarchyMap.put("ROLE_ADMIN", asList("ROLE_MANAGER"));
		roleHierarchyMap.put("ROLE_MANAGER", asList("ROLE_STAFF"));
		roleHierarchyMap.put("ROLE_STAFF", asList("ROLE_USER"));

		String hierarchyFromMap = RoleHierarchyUtils.roleHierarchyFromMap(roleHierarchyMap);
		roleHierarchy.setHierarchy(hierarchyFromMap);

		return roleHierarchy;
	}

	private List<String> asList(String... a) {
		return Arrays.asList(a);
	}
}
