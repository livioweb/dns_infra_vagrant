package com.tarefa.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.tarefa.demo.DemoApplication;
@SpringBootTest
class DemoApplicationTests {

	@Test
	public void testMain() throws Exception {
		DemoApplication.main(new String[]{"args"});
	}
	
	@Test
	public void testSoma(){
		System.out.println("Soma");
	   		int a = 1;
			int b = 1;
			int expResult = 2;
			int result = DemoApplication.Soma(a, b);
			assertEquals(expResult, result);
   }

}