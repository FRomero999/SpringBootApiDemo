package com.example.springbootapidemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}/edit")
    /**
     * Va a la página de edición con un formulario
     */
    public String edit(@PathVariable Long id, Model model){
        if( repo.existsById(id)){
            model.addAttribute("juego",repo.findById(id).get());
            return "edit";
        } else {
            return "redirect:/web";
        }
    }

    @PostMapping("/{id}/edit")
    /**
     * Recibe los datos del formulario y actualiza
     */
    public String editPost(@PathVariable Long id, @ModelAttribute Juego datos){
        System.out.println(datos);
        repo.save(datos);
        return "redirect:/web/"+id;
    }

    @GetMapping("/new")
    /**
     * Muestra el formulario vacio
     */
    public String newJuego(Model model){
        Juego nuevo = new Juego();
        model.addAttribute(nuevo);
        return "edit";
    }

    @PostMapping("/new")
    /**
     * Guarda el juego creado
     */
    public String newJuego(@ModelAttribute Juego datos){
        Juego nuevo = new Juego();
        repo.save(datos);
        return "redirect:/web/"+datos.getId();
    }
}
