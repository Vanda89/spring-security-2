package fr.diginamic.springsecurityj2.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CookieController {

    @GetMapping("/get-cookie")
    public ResponseEntity<String> getCookie(HttpServletRequest request) {
        String cookieName = request.getParameter("cookieName");
        String cookieValue = request.getParameter("cookieValue");
        if (cookieName == null || cookieName.isBlank()) {
            return ResponseEntity.badRequest().body("Le paramètre cookieName est obligatoire");
        }

        ResponseCookie tokenCookie = ResponseCookie.from(
                cookieName,
                cookieValue).build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        tokenCookie.toString()
                )
                .body("cookie posé avec succès");
    }
}
