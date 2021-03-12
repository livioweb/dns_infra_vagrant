package com.tarefa.demo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.tarefa.demo.HelloWorldController;

@SpringBootTest
class HelloWorldControllerTests {

    private HelloWorldController controller;

    @Test
    public void testRetornoIp(){
        controller = new HelloWorldController();
        controller.getIp();
    }

	@Test
    public void fazendoNada(){
        controller = new HelloWorldController();
        controller.healthCheck();
    }

   /* @Test
    public void fazendoNada2(){
        controller = new HelloWorldController();
        controller.get();
    }

    @Test
    public void fazendoNada3(){
        controller = new HelloWorldController();
        controller.getClass().getName();
    }*/


}
