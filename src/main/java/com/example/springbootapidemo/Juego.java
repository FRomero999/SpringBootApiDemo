package com.example.springbootapidemo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="coleccionjuegos")
public class Juego {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(name="anio")
    private Integer año;
    private String categoria;
    private String plataforma;
}
