package com.in5bm.soccerfieldmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

    @Bean
    Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dcrgnm3ud",
                "api_key", "655453188429764",
                "api_secret", "7DJJn3dB1hGQNAYYY0xM81Xr05M"
        ));
    }
}
