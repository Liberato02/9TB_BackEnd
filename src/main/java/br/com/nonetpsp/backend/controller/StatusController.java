package br.com.nonetpsp.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @GetMapping("/api/status")
    public String status() {
        return "Backend 9 TPSP funcionando!";
    }
}