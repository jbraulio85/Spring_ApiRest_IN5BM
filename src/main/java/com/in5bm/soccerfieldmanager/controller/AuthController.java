package com.in5bm.soccerfieldmanager.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.in5bm.soccerfieldmanager.DTOs.UserRegisterDTO;
import com.in5bm.soccerfieldmanager.models.User;
import com.in5bm.soccerfieldmanager.services.AuthService;
import com.in5bm.soccerfieldmanager.services.CloudinaryService;
import com.in5bm.soccerfieldmanager.utils.PasswordEncrypt;

import jakarta.validation.Valid;




@RestController
@RequestMapping("soccerfield/v1/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestPart("profilePicture") MultipartFile profilePicture,
        @Valid @ModelAttribute UserRegisterDTO userDto,
        BindingResult result){
        
        Map<String, Object> res = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
            res.put("Errores", errors);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        try {
            logger.info("Enviando el archivo a cloudinary");
            Map<String, Object> uploadResult = cloudinaryService.uploadImg(profilePicture, "profiles");
            String profilePhoto = uploadResult.get("url").toString();
            String img = profilePhoto.substring(profilePhoto.indexOf("profiles/"));
            User user = new User(userDto,img);
            user.setId(UUID.randomUUID().toString());
            authService.save(user);
            logger.info("Usuario agregado exitosamente");
            res.put("Mensaje", "Usuario agregado exitosamente");
            res.put("Usuario", user);
            return new ResponseEntity<>(res,HttpStatus.CREATED);
        } catch (IOException e) {
            logger.error("Error de entrada de archivos");
            res.put("Mensaje", "Error al subir la imagen");
            res.put("Error",e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch(CannotCreateTransactionException e){
            logger.error("Error al procesar la transacción");
            res.put("Mensaje", "Error al crear la transacción");
            res.put("Error",e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.SERVICE_UNAVAILABLE);
        } catch(DataAccessException e){
            logger.error("Error al conectar a la base de datos");
            res.put("Mensaje", "Error conectar a la base de datos");
            res.put("Error",e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user, BindingResult result){
        Map<String, Object> res = new HashMap<>();

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
            res.put("Errores", errors);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        try {
            User existingUser = authService.login(user.getEmail());
            if(existingUser == null || !PasswordEncrypt.verifyPassword(user.getPassword(), existingUser.getPassword())){
                res.put("Mensaje", "Usuario o contraseña incorrectas");
                return new ResponseEntity<>(res,HttpStatus.UNAUTHORIZED);
            }

            res.put("Mensaje", "Bienvenido " + existingUser.getUsername());
            res.put("Usuario", existingUser);
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        }  catch(CannotCreateTransactionException e){
            logger.error("Error al procesar la transacción");
            res.put("Mensaje", "Error al crear la transacción");
            res.put("Error",e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.SERVICE_UNAVAILABLE);
        } catch(DataAccessException e){
            logger.error("Error al conectar a la base de datos");
            res.put("Mensaje", "Error conectar a la base de datos");
            res.put("Error",e.getMessage());
            return new ResponseEntity<>(res, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
    

