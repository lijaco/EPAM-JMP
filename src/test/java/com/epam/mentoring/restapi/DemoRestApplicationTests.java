package com.epam.mentoring.restapi;

import com.epam.mentoring.restapi.controller.HelloControllor;
import com.epam.mentoring.restapi.repository.EmployeeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoRestApplication.class)   //MockServletContext
@WebAppConfiguration
public class DemoRestApplicationTests {

	private MockMvc mvc;

	@Autowired
    private EmployeeRepository repository;
	
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new HelloControllor()).build();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void helloTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/hello/abc").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(equalTo("hello, abc")));
	}


}