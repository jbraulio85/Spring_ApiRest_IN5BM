package com.in5bm.soccerfieldmanager.DTOs;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDTO implements Serializable{

    private String id;
    @NotBlank(message = "El correo es obligatorio")
    @Email(message="Ingresa una dirección de correo válida")
    private String email;
    @NotBlank(message="El usuario es obligatorio")
    private String password;
    @NotBlank(message="El usuario es obligatorio")
    private String username;
    private String name;
    private String surname;
}
