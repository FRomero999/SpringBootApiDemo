package com.example.springbootapidemo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
 *  Implementa autorización para las operaciones por medio de la sesión.
 *
 *  Antes de realizar una operación de escritura, el cliente debe acceder a login
 *
 *  [POST] -> creación
 *  [PUT] -> actualización
 *  [DELETE] -> borrado
 *
 */
@RequestMapping("/v4")
public class JuegoControllerV4 {
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

    @GetMapping("/login")
    public ResponseEntity login(HttpServletRequest request){
        HttpSession s = request.getSession();
        s.setAttribute("login",true);
        return new ResponseEntity<String>("Successfully logged!",HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        HttpSession s = request.getSession();
        s.removeAttribute("login");
        return new ResponseEntity<String>("Successfully logged out!",HttpStatus.OK);
    }

    /**
     * Crea un nuevo juego solo si está logueado
     */
    @PostMapping("/")
    public ResponseEntity nuevo(@RequestBody Juego juego, HttpServletRequest request){

        HttpSession s = request.getSession();

        var login = s.getAttribute("login");
        ResponseEntity salida = new ResponseEntity<String>("Not logged",HttpStatus.UNAUTHORIZED);

        if( (login!=null) && (boolean)login ) {
                salida = new ResponseEntity<Juego>(repo.save(juego), HttpStatus.OK);
        }
        return salida;
    }


}
