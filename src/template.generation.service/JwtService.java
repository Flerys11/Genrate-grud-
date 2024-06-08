package template.generation.service;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;;
import template.generation.model.AuthenticationResponse;
import template.generation.repository.model.users;
import template.generation.model.usersRepository;
import template.generation.repository.model.token;
import template.generation.model.tokenRepository;


@Service
public class JwtService
{
	 private final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c"; 
	 private final TokenRepository tokenRepository; 

	 public JwtService(TokenRepository  tokenRepository) { 
	  this.tokenRepository  = tokenRepository; 
	 } 

	 public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	 public boolean isValid(String token, UserDetails user) {
		String username = extractUsername(token);

		boolean validToken = tokenRepository
				.findByToken(token)
				.map(t -> !t.isLoggedOut())
				.orElse(false);

		return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
	}

	 private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	 private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	 public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}


	 public String generateToken(Users users) { 
	 String token = Jwts
		.builder()
		.subject(users.getUsername())
		.issuedAt(new Date(System.currentTimeMillis()))
		.expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
		.signWith(getSigninKey())
		.compact();

	 return token;
 	} 

	 private SecretKey getSigninKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	 }


}

