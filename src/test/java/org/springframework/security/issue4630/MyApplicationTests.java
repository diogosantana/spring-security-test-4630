package org.springframework.security.issue4630;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyApplicationTests {

	@Autowired
	WebApplicationContext context;

	@Autowired
	FilterChainProxy filterChain;

	MockMvc mvc;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).addFilters(filterChain).build();
	}

	@Test
	public void ok() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		mvc.perform(get("/api/domains").with(user("user").password("user").roles("STAFF")).//
				headers(headers)).//
				andExpect(status().isOk()).//
				andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON));
	}

	@Test
	public void indexHtml() throws Exception {
		mvc.perform(get("/index.html")).//
				andExpect(status().isOk()).//
				andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML_VALUE));
	}

	@Test
	public void roledHtml() throws Exception {
		mvc.perform(get("/roled.html")).//
				andExpect(status().isUnauthorized()).//
				andExpect(status().reason(is("Acesso negado")));
	}

	@Test
	public void forbidden() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);

		  mvc.perform(post("/api/domains").//
				headers(headers)).//
				andExpect(status().isUnauthorized()).//
				andExpect(status().reason(is("Acesso negado")));
	}

}
