package com.encurtador.encurtador_url.service;

import com.encurtador.encurtador_url.model.Url;
import com.encurtador.encurtador_url.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private final Random random = new Random();

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private String generateShortCode(){
        int length = 6;
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    public Url createShortUrl(String originalUrl){
        String shortCode;

        do{
            shortCode = generateShortCode();
        } while (urlRepository.findByShortCode(shortCode).isPresent());

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);
        url.setCreatedAt(LocalDateTime.now());

        return urlRepository.save(url);
    }

    public Optional<Url> getUrlByShortCode(String shortCode){
        return urlRepository.findByShortCode(shortCode);
    }

}
