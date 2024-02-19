package com.example.springbootapidemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;

/*
 * @RestController se encarga de convertir automaticamente la salida a JSON y devolverla en una respuesta HTTP
 */
@RestController

/**
 *  API REST de solo lectura
 *
 *  Solo implementa los métodos básicos de lectura con GET
 *  y devuelve los datos en formato JSON.
 */
@RequestMapping("/v1")
public class JuegoControllerV1 {
    @Autowired
    private JuegoRepository repo;

    @GetMapping("/")
    /**
     *  Devuelve todos los juegos sin filtrar
     * */
    public List<Juego> home() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Juego> get(@PathVariable Long id){
        if( repo.existsById(id)){
            return new ResponseEntity<Juego>( repo.findById(id).get(), HttpStatus.OK );
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("categoria/{categoria}")
    public List<Juego> getCategoria(@PathVariable String categoria){
        return repo.getJuegosByCategoria(categoria);
    }

    @GetMapping("plataforma/{plataforma}")
    public List<Juego> getPlataforma(@PathVariable String plataforma){
        return repo.getJuegosByPlataforma(plataforma);
    }

    @GetMapping("anterior/{año}")
    public List<Juego> getAnterior(@PathVariable Integer año){
        return repo.getJuegosByAñoBefore(año);
    }

    @GetMapping("plataformas")
    public List<String> plataformas(){
        return repo.plataformas();
    }

}
