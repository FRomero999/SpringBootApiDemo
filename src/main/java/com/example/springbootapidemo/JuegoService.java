package com.example.springbootapidemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuegoService {

    @Autowired
    JuegoRepository repo;

    public Juego modificarCrearJuego(Long id, Juego juegonuevo) {
        var juego = new Juego();

        var optionalJuego = repo.findById(id);

        if(optionalJuego.isEmpty()){
            juego = juegonuevo;
        } else{
            juego = optionalJuego.get();
            if(juegonuevo.getNombre()!=null) juego.setNombre( juegonuevo.getNombre() );
            if(juegonuevo.getAño()!=null) juego.setAño( juegonuevo.getAño() );
            if(juegonuevo.getCategoria()!=null) juego.setCategoria( juegonuevo.getCategoria() );
            if(juegonuevo.getPlataforma()!=null) juego.setPlataforma( juegonuevo.getPlataforma() );
        }
        return repo.save(juego);
    }

}
