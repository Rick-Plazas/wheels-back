package com.universdad.wheels.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //Refactoring

public class Usuario{
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Genera el id incrementalmente con Postgres al meter otro usuario
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido; 

    @Email
    @Column(unique = true)
    private String correo;

    @NotBlank
    private String contrasena;

    private String telefono;

    private String foto; //URL o nombre de la foto

    //Guarda el tippo rol como cadena de texto en la bd
    @Enumerated(EnumType.STRING) 
    private Rol rol; //Pasajero, conductor

    //Rleación 1-1 con el vehículo si es conductor
    @OneToOne(mappedBy = "conductor", cascade = CascadeType.ALL) //Si se elimina al usuario, también al carro
    private Vehiculo vehiculo;

    //Rlelación 1-N con viajes para condcuctor
    
    @OneToMany(mappedBy = "conductor", cascade=CascadeType.ALL)
    @JsonIgnore
    private List<Viaje> viajes;

    //Relación 1-N con viajes para pasajero (Un pasajero solo está asociaco a un viaje)
    @OneToMany(mappedBy = "pasajero", cascade = CascadeType.ALL)
    private List<Reserva> reservas;

    public enum Rol{
        PASAJERO,
        CONDUCTOR
    }
    
}
