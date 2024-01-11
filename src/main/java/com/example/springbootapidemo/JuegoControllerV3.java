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
 *  API REST de lectura/escritura con seguridad básica basada en token
 *
 *  Implementa operaciones de escritura: creación, actualización y borrado
 *
 *  Solo los métodos de escritura requieren token
 *
 *  [POST] -> creación
 *  [PUT] -> actualización
 *  [DELETE] -> borrado
 *
 */
@RequestMapping("/v3")
public class JuegoControllerV3 {
    @Autowired
    private JuegoRepository repo;

    @Autowired
    private SecurityService security;

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
     * Crea un nuevo juego, pero solo si el token es válido
     */
    @PostMapping("/")
    public ResponseEntity<Juego> nuevo(@RequestBody Juego juego, @RequestParam String token){
        if( security.validateToken(token) )
            return new ResponseEntity<Juego>(repo.save(juego),HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/{id}")
    /**
     * Actualiza un juego que ya exista, o lo guarda si no existe (solo si el token es correcto)
     */
    public ResponseEntity<Juego> put(@PathVariable Long id, @RequestBody Juego juegonuevo, @RequestParam String token){

        if( !security.validateToken(token) ){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else{
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

            return new ResponseEntity<Juego>(repo.save(juego),HttpStatus.OK);
        }

    }


    @DeleteMapping("/{id}")
    /**
     * Elimina un juego
     */
    public ResponseEntity<Juego> delete(@PathVariable Long id,  @RequestParam String token){

        ResponseEntity<Juego> respuesta = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if( security.validateToken(token) ){
            Juego salida = new Juego();
            if (repo.existsById(id)) {
                salida = repo.findById(id).get();
                repo.deleteById(id);
                respuesta = new ResponseEntity<Juego>(salida, HttpStatus.OK);
            } else {
                respuesta = new ResponseEntity<Juego>(salida, HttpStatus.NOT_FOUND);
            }
        }
        return respuesta;
    }
}
