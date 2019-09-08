package com.beerhouse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.BeerApplication;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackageClasses = BeerApplication.class)
public class BeerApplicationTests {

	@Test
	public void contextLoads() {
		//Links p/ apoio: https://blog.jayway.com/2014/01/14/unit-testing-spring-mvc-controllers-with-rest-assured/
		//https://www.mkyong.com/spring-boot/spring-rest-integration-test-example/
		//https://www.blazemeter.com/blog/spring-boot-rest-api-unit-testing-with-junit/
	}

}