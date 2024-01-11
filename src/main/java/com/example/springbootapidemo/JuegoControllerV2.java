package com.example.springbootapidemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @RestController se encarga de convertir automaticamente la salida a JSON y devolverla en una respuesta HTTP
 */
@RestController

/**
 *  API REST de lectura/escritura
 *
 *  Implementa operaciones de escritura: creación, actualización y borrado
 *
 *  [POST] -> creación
 *  [PUT] -> actualización
 *  [DELETE] -> borrado
 *
 */
@RequestMapping("/v2")
public class JuegoControllerV2 {
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
    /**
     * Devuelve un solo juego
     */
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

    /**
     * Crea un nuevo juego
     */
    @PostMapping("/")
    public Juego nuevo(@RequestBody Juego juego){
        return repo.save(juego);
    }

    @PutMapping("/{id}")
    /**
     * Actualiza un juego que ya exista, o lo guarda si no existe
     */
    public Juego put(@PathVariable Long id, @RequestBody Juego juegonuevo){

        var juego = new Juego();

        var optionalJuego = repo.findById(id);

        if(optionalJuego.isEmpty()){
            juego = juegonuevo;
        } else{
            juego = optionalJuego.get();
            juego.setNombre( juegonuevo.getNombre() );
            juego.setAño( juegonuevo.getAño() );
            juego.setCategoria( juegonuevo.getCategoria() );
            juego.setPlataforma( juegonuevo.getPlataforma() );
        }

        return repo.save(juego);
    }


    @DeleteMapping("/{id}")
    /**
     * Elimina un juego
     */
    public ResponseEntity delete(@PathVariable Long id){
        if( repo.existsById(id)){
            repo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK );
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }
}
