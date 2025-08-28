package fr.diginamic.springsecurityj2.controllers;

import fr.diginamic.springsecurityj2.entity.UserApp;
import fr.diginamic.springsecurityj2.repositories.UserAppRepositories;
import fr.diginamic.springsecurityj2.services.JWTAuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CookieController {

    private final JWTAuthService jwtService;
    private final UserAppRepositories userAppRepositories;
    private final BCryptPasswordEncoder passwordEncoder;


    /**
     * Constructor for CookieController.
     **
     * @param jwtService the service used to generate and validate JWT tokens
     * @param userAppRepositories the repository used to access UserApp entities in the database
     * @param passwordEncoder the encoder used to hash and verify user passwords
     */
    public CookieController(JWTAuthService jwtService, UserAppRepositories userAppRepositories, BCryptPasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userAppRepositories = userAppRepositories;
        this.passwordEncoder = passwordEncoder;
    }


    /**
     * Retrieves a cookie with the specified name and value from the request parameters
     * and sets it in the response.
     *
     * @param request the HttpServletRequest containing the cookieName and cookieValue parameters
     * @return a ResponseEntity containing a success message and the Set-Cookie header,
     *         or a bad request response if the cookieName is missing
     */
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

    /**
     * Reads all cookies from the incoming HTTP request and returns the value
     * of the cookie named "cookieName" if present.
     *
     * @param request the HttpServletRequest containing the cookies
     * @return a String with the cookie value if found, or a message indicating
     *         that the cookie was not found
     */
    @GetMapping("/raw")
    public String rawCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookieName")) {
                    return "cookieName = " + cookie.getValue();
                }
            }
        }
        return "Cookie non trouvé";
    }

    /**
     * Handles user login requests. For now, it simply returns a success message
     * with the provided username.
     *
     * @param user the UserApp object containing username and password
     * @return a ResponseEntity containing a message indicating login success
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody UserApp user) {
        return userAppRepositories.findByUsername(user.getUsername())
                .map(dbUser -> {
                    if (passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
                        String token = jwtService.generateToken(dbUser.getUsername());
                        return ResponseEntity.ok(token);
                    } else {
                        return ResponseEntity.status(401).body("Mot de passe incorrect");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(404).body("Utilisateur non trouvé"));
    }


    /**
     * Generates a JWT for the given user.
     *
     * @param user the UserApp object containing the username
     * @return a JWT string representing the user
     */
    @PostMapping("/create-jwt")
    @ResponseBody
    public String createJwt(@RequestBody UserApp user) {
        return jwtService.generateToken(user.getUsername());
    }

    /**
     * Returns the last generated JWT.
     *
     * @return the last JWT as a String
     */
    @GetMapping("/get-jwt")
    @ResponseBody
    public String getJwt() {
        return jwtService.getLastToken();
    }

    /**
     * Verifies whether a given JWT is valid.
     *
     * @param token the JWT string to verify
     * @return ResponseEntity containing true if valid, false if invalid
     */
    @GetMapping("/verify-jwt/{token}")
    public ResponseEntity<Boolean> verifyJwt(@PathVariable String token) {
        try {
            Claims claims = jwtService.validateToken(token);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
