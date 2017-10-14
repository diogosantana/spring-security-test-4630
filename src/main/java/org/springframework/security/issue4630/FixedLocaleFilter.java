package org.springframework.security.issue4630;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class FixedLocaleFilter implements Filter {

	@Autowired
	private LocaleResolver localeResolver;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LocaleContextHolder.setLocale(localeResolver.resolveLocale((HttpServletRequest) request), false);
		try {
			chain.doFilter(request, response);

		} finally {
			LocaleContextHolder.resetLocaleContext();
		}
	}

	@Override
	public void destroy() {

	}

}
