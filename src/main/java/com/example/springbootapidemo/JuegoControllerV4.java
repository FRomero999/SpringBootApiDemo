package com.example.springbootapidemo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @GetMapping("/{id}/lite")
    /**
     * Devuelve un solo juego
     */
    public ResponseEntity getLite(@PathVariable Long id){
        if( repo.existsById(id)){
            Juego  j = repo.findById(id).get();
            HashMap salida = new HashMap();
            salida.put("status","ok");
            salida.put("name",j.getNombre());
            salida.put("antigüedad",2024-j.getAño());
            return new ResponseEntity<>(salida, HttpStatus.OK );
        } else {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND );
        }
    }

    @GetMapping("/login/{token}")
    public ResponseEntity login(HttpServletRequest request,@PathVariable String token){
        HttpSession s = request.getSession();
        if( security.validateToken(token) ) {
            s.setAttribute("login", true);
            return new ResponseEntity<>("Successfully logged!", HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request){
        HttpSession s = request.getSession();
        s.setAttribute("login",false);
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

    @GetMapping("/sumar")
    public ResponseEntity sumar(@RequestParam Integer s1, @RequestParam Integer s2){
        return new ResponseEntity<String>("La suma es "+ (s1+s2),HttpStatus.OK);
    }


}
