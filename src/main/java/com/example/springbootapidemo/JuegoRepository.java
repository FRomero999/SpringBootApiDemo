package com.example.springbootapidemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {
    public List<Juego> getJuegosByCategoria(String categoria);

    public List<Juego> getJuegosByPlataforma(String plataforma);

    public List<Juego> getJuegosByAñoBefore(Integer año);

    @Query("SELECT DISTINCT(j.plataforma) FROM Juego j")
    public List<String> plataformas();
}

