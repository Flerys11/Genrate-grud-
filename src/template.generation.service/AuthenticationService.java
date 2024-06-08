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
public class AuthenticationService
{
	private final UsersRepository usersRepository;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	private final PasswordEncoder passwordEncoder;

	public AuthenticationService(Usersrepository usersRepository, Tokenrepository tokenRepository, Jwtservice jwtService, Passwordencoder passwordEncoder, Authenticationmanager authenticationManager )
	{
		this.usersRepository = usersRepository;
		this.tokenRepository = tokenRepository;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}
	public AuthenticationResponse registre(Users request){
	 if(usersRepository.findByUsername(request.getUsername()).isPresent()){
		return new AuthenticationResponse(null, "users already exist");
	}
	 Users users = new Users();
	 users.setFirstName(request.getFirstName());
	 users.setLastName(request.getLastName());
	 users.setUsername(request.getUsername());
	 users.setPassword(passwordEncoder.encode(request.getPassword()));
	 users.setRole(request.getRole());
	 users = usersRepository.save(users);
	 String jwt = jwtService.generateToken(users);
	 saveUserToken(jwt, users);
	 return new AuthenticationResponse (jwt, "users registration was successful");
	}
	public AuthenticationResponse registre(Users request){
	 authenticationManager.authenticate(
	  new UsernamePasswordAuthenticationToken( 
	   requestgetUsername(), 
	   requestgetPassword() 
 	 ) 
 	);	 Users request = usersRepository.findByUsername(request.getUsername()).orElseThrow(); 
	 String jwt = jwtService.generateToken(users); 
	 revokeAllTokenByUsers(users); 
	 saveUserToken(jwt, users); 
	 return new AuthenticationResponse(jwt, "users login was successful");
	}

	private void revokeAllTokenByUser(Users users) {
	 List<Token> validTokens = tokenRepository.findAllTokensByUsers(users.getId()); 
	 if(validTokens.isEmpty()) { 
	  return; 
	 }	 validTokens.forEach(t-> { 
	  t.setLoggedOut(true); 
	 }); 
	 tokenRepository.saveAll(validTokens); 
	}
	private void saveUserToken(String jwt, Users users) {
	 Token token = new Token(); 
	 token.settoken(jwt); 
	 token.setLoggedOut(false); 
	 token.setUsers(users); 
	 tokenRepository.save(token); 
	 }

}

