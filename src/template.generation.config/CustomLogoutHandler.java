package template.generation.config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import template.generation.repository.TokenRepository;
import template.generation.model.Token;


@Service
public class CustomLogoutHandler implements LogoutHandler
{

        private final TokenRepository tokenRepository;

        public CustomLogoutHandler(TokenRepository tokenRepository) {
            this.tokenRepository = tokenRepository;
        }
    

        @Override
        public void logout(HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) {
            String authHeader = request.getHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                return;
            }

            String token = authHeader.substring(7);
            Token storedToken = tokenRepository.findByToken(token).orElse(null);

            if(storedToken != null) {
                storedToken.setLoggedOut(true);
                tokenRepository.save(storedToken);
            }
        }
    

}

