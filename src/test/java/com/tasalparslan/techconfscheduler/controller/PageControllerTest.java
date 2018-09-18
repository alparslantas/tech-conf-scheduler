package com.tasalparslan.techconfscheduler.controller;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PageControllerTest {

	MockMvc mockMvc;

	@Mock
	private PageController pageController;

	@Autowired
	private TestRestTemplate template;

	@Test
	public void getScheduledTalks() {
		ResponseEntity<String> response = template.getForEntity("/agenda-page", String.class);
		Assert.assertEquals(200, response.getStatusCode().value());
	}

}
