package com.in5bm.soccerfieldmanager.models;

import java.io.Serializable;

import com.in5bm.soccerfieldmanager.DTOs.UserRegisterDTO;
import com.in5bm.soccerfieldmanager.utils.PasswordEncrypt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Data
@Table(name = "user")
public class User implements Serializable {
   @Id
   @Column(name="id")
    private String id;

    @Email(message="Debe de ingresar un email válido")
    @Column(unique = true, name="email")
    private String email;

    @Column(unique = true)
    private String username;

    private String name;
    private String surname;
    private String password;
    private String profilePhoto;

    public User(UserRegisterDTO userDTO, String img){
        this.email = userDTO.getEmail();
        this.username = userDTO.getUsername();
        this.password = PasswordEncrypt.encryptPassword(userDTO.getPassword());
        this.name = userDTO.getName();
        this.surname = userDTO.getSurname();
        this.profilePhoto = img;
    }
}
