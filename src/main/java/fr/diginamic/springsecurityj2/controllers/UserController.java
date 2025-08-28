package fr.diginamic.springsecurityj2.controllers;

import fr.diginamic.springsecurityj2.entity.UserApp;
import fr.diginamic.springsecurityj2.repositories.UserAppRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserAppRepositories userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs a new UserController with the given user repository and password encoder.
     *
     * @param userRepo the repository used to manage UserApp entities in the database
     * @param passwordEncoder the BCryptPasswordEncoder used to hash user passwords
     */
    public UserController(UserAppRepositories userRepo, BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     *
     * @param user the UserApp object containing the username and password
     * @return a ResponseEntity with a success message if registration succeeded,
     *         or a bad request message if the username is already taken
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserApp user) {
        if(userRepo.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username déjà pris");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return ResponseEntity.ok("Utilisateur créé avec succès");
    }
}