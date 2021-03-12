package com.tarefa.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/tarefa")
public class HelloWorldController {

	private static final String HELLO_WORLD = "Apresentando o projeto deploy {PRECISA DO ROLLBACK AGORA ====V3 Apresentacao====== }";


    @GetMapping("/ip")
    public ResponseEntity<String> getIp() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        String ip = request.getRemoteAddr();
        return ResponseEntity.ok("Aplicando test ... " + ip);
    }


    private Logger log = Logger.getLogger(HelloWorldController.class.getName());
    @GetMapping("/hello-world")


    public ResponseEntity<String> get(){
        return ResponseEntity.ok(HELLO_WORLD);
    }

    public void healthCheck(){
        log.info("200 OK");
    }

}
