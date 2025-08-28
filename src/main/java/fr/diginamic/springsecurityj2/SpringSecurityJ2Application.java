package fr.diginamic.springsecurityj2;

import fr.diginamic.springsecurityj2.services.JWTAuthService;
import io.jsonwebtoken.Claims;
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
	private JWTAuthService jwtService;

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
		var context = SpringApplication.run(SpringSecurityJ2Application.class, args);

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
	/*	System.out.println();
		for (String s : prenoms) {
			String hash = encoder.encode(s);
			System.out.println(s + " -> " + hash);
		}
		String totohaseh = encoder.encode("toto");
		String newotohaseh = encoder.encode("toto");
		System.out.println(encoder.matches("toto", totohaseh));
		System.out.println(encoder.matches("toto", newotohaseh));*/

		JWTAuthService jwtService = context.getBean(JWTAuthService.class);
		String message = "voici une chaine à chiffrer";

		String token1 = jwtService.generateToken(message);
		System.out.println("JWT généré : " + token1);

		Claims claims = jwtService.validateToken(token1);
		System.out.println("Message extrait du JWT : " + claims.get("message"));

		List<String> tokens = List.of(
				"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGVtcGxlIiwibWVzc2FnZSI6IlZvaWNpIHVuZSBjaGHDrm5lIMOgIHNpZ25lciIsImlhdCI6MTc0NDgzMTMxNX0.VIBNB1C1j93PUDrbmFJwbJXXbTYNPwEGJbkEQHVZoYg",
				"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGVtcGxlIiwibWVzc2FnZSI6IlZvaWNpIHVuZSBjaGHDrm5lIMOgIHNpZ25lciIsImlhdCI6MTc0NDgzMTYwNn0.UZ6IO0Wvrnd4NP63diYjyvkNFNWI1NfDGP9lpfJyJSE",
				"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGVtcGxlIiwibWVzc2FnZSI6IlZvaWNpIHVuZSBjaGHDrm5lIMOgIHRpZ25lciIsImlhdCI6MTc0NDgzMTY0NX0.5vpcu1T7DmXDuoCBLhBJAQGE3HpNUO41-Tr0rkGrDY0",
				"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJleGVtcGxlIiwibWVzc2FnZSI6IlZvaWNpIHVuZSBjaGHDrm5lIMOgIHNpZ25lciIsImlhdCI6MTc0NDgzMTY3OH0.NXvOGwyMKQ9tK2z5dR6ER5tbf2plLlkxgJnCQ0lI13g"
		);
		for (String token : tokens) {
			try {
				Claims claims2 = jwtService.validateToken(token);
				System.out.println("JWT valide : " + token);
				System.out.println("Message extrait : " + claims2.get("message"));
			} catch (Exception e) {
				System.out.println("JWT invalide : " + token);
			}
		}

		/**
		 * Header = eyJhbGciOiJIUzI1NiJ9 =
		 * {
		 *   "alg": "HS256"
		 * }
		 * Payload = eyJzdWIiOiJKZSBuZSBzYWlzIHBhcyIsIm1lc3NhZ2UiOiLDoCBtb2kgbcOqbWUiLCJtZXNzYWdlLWNhY2jDqSI6InRyYXZhaWxsZSwgw6dhIGZpbml0IHRvdWpvdXJzIHBhciBwYXllciIsImxodW1vdXIiOiJjJ2VzdCBpbXBvcnRhbnQiLCJpYXQiOjE3NDQ4MzE5MTV9
		 * Subject = Je ne sais pas,
		 * Autres messages =
		 * "message": "Ô moi même",
		 * "message-caché": "travail, ça finit toujours par payer",
		 * "lhumour": "c'est important",
		 *  "iat": 1744831915
		 *  Signature : wdaFguIzdkNKgVaYmSg5jHgYCDenufwjlJEL7T42fLA
		 *  essayeDoncCetteChouetteCleSecreteOnVerraSiCaMarche invalide
		 *  maToutAutantChouetteCleSecreteQueJeChoisiCommeJeVeux secret key
		 */
		String tokens2 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJKZSBuZSBzYWlzIHBhcyIsIm1lc3NhZ2UiOiLDoCBtb2kgbcOqbWUiLCJtZXNzYWdlLWNhY2jDqSI6InRyYXZhaWxsZSwgw6dhIGZpbml0IHRvdWpvdXJzIHBhciBwYXllciIsImxodW1vdXIiOiJjJ2VzdCBpbXBvcnRhbnQiLCJpYXQiOjE3NDQ4MzE5MTV9.wdaFguIzdkNKgVaYmSg5jHgYCDenufwjlJEL7T42fLA";






	}

}
