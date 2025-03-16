package se2.group6.librarymanagement.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryCfg {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dlqpdl4mz",
                "api_key", "461146261413479",
                "api_secret", "YJ03eeKoRH21wUx3dyPStyUK4pw",
                "secure", true
        ));
    }
}
