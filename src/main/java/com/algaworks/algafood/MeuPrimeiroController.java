package com.algaworks.algafood;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("prod")
public class MeuPrimeiroController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello";
    }
}
