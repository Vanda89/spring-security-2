package fr.diginamic.springsecurityj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

@SpringBootApplication
public class SpringSecurityJ2Application {

    public static String getHash(String input) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            String hex = HexFormat.of().formatHex(hashBytes);
            return hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        List<String> prenoms = List.of(
                "Sandrine", "Cyril", "Nuno", "Laurence", "Mathieu", "Dmitry", "Tommy", "Sarah", "Robin", "Angeline", "Julien", "Daris", "Toto"
        );
        System.out.println("SHA-256 des prénoms : ");
        for (String prenom : prenoms) {
            String hash = getHash(prenom);
            System.out.println(prenom + " -> " + hash);
        }
        System.out.println();
        System.out.println("Hashes BCrypt :");
        for (String s : prenoms) {
            String hash = encoder.encode(s);
            System.out.println(s + " -> " + hash);
        }
        System.out.println();
        for (String s : prenoms) {
            String hash = encoder.encode(s);
            System.out.println(s + " -> " + hash);
        }
        String totohaseh = encoder.encode("toto");
        String newotohaseh = encoder.encode("toto");
        System.out.println(encoder.matches("toto", totohaseh));
        System.out.println(encoder.matches("toto", newotohaseh));
        SpringApplication.run(SpringSecurityJ2Application.class, args);
    }

}
