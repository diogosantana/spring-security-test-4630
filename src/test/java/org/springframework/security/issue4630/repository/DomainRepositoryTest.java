package org.springframework.security.issue4630.repository;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.issue4630.DomainRepository;
import org.springframework.security.issue4630.SecurityUtils;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DomainRepositoryTest {

	@Autowired DomainRepository domainRepository;
	@Test(expected=AccessDeniedException.class)
	public void test() {
		SecurityUtils.runAs("user", "user", "ROLE_USER");
		
		domainRepository.deleteAll();
		fail("Expected a security error");
	}

}
