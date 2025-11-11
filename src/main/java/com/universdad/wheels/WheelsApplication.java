package com.universdad.wheels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
public class WheelsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WheelsApplication.class, args);
	}

	/*@Bean
	CommandLineRunner initDatabase(UsuarioRepository usuarioRepository){
		return args -> {
			//Usario de prueba
			Usuario u = Usuario.builder()
			.nombre("Ricardo")
			.apellido("Plazas")
			.correo("rplazas200@hotmail.com")
			.telefono("3015614293")
			.contraseña("1234")
			.build();

			usuarioRepository.save(u);

			System.out.println("Usuario guardado: "+u);
		}; 
	}
*/
}
