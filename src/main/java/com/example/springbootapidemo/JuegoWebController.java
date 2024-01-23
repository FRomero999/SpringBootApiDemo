package com.example.springbootapidemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * @Controller se encarga de convertir la salida a HTML
 * usando plantillas de ThymeLeaf.
 */
@Controller

/**
 *  API REST de solo lectura
 *
 *  Solo implementa los métodos básicos de lectura con GET
 *
 */
@RequestMapping("/web")
public class JuegoWebController {
    @Autowired
    private JuegoRepository repo;

    @GetMapping("")
    /**
     *  Devuelve todos los juegos sin filtrar
     * */
    public String home(Model model) {
        model.addAttribute("juegos",repo.findAll());
        return "home";
    }

    @GetMapping("/{id}")
    /**
     * Devuelve un solo juego
     */
    public String get(@PathVariable Long id, Model model){
        if( repo.existsById(id)){
            model.addAttribute("juego",repo.findById(id).get());
            return "juego";
        } else {
            return "redirect:/";
        }
    }

}
