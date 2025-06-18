package icu.lry.ordersystem.utils;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Component
public class MaxSize {

    @Bean
    public MultipartConfigElement multipartConfigElement(@Value("${spring.servlet.multipart.max-file-size}") String maxFileSize,
                                                         @Value("${spring.servlet.multipart.max-request-size}") String maxRequestSize) {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse(maxFileSize));
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));
        return factory.createMultipartConfig();
    }
}
