package com.encurtador.encurtador_url.controller;

import com.encurtador.encurtador_url.dto.UrlRequest;
import com.encurtador.encurtador_url.model.Url;
import com.encurtador.encurtador_url.service.UrlService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shortem")
    public ResponseEntity<Url> shortenUrl(@RequestBody UrlRequest request) {
        Url shortened = urlService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.ok(shortened);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Object> redirectToOriginal(@PathVariable String shortCode) {
        return urlService.getUrlByShortCode(shortCode)
                .map(url -> {
                    String originalUrl = url.getOriginalUrl();
                    // Se a URL original não começar com http ou https, adicione "http://" (ou trate adequadamente)
                    if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
                        originalUrl = "http://" + originalUrl;
                    }
                    return ResponseEntity.status(302).location(URI.create(originalUrl)).build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
