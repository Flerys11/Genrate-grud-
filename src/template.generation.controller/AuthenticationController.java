package template.generation.controller;
import com.example.authet.model.AuthenticationResponse;
import com.example.authet.model.Users;
import com.example.authet.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController
{

        private final AuthenticationService authService;

        public AuthenticationController(AuthenticationService authService) {
            this.authService = authService;
        }


        @PostMapping("/register")
        public ResponseEntity<AuthenticationResponse> register(
                @RequestBody Users request
        ) {
            return ResponseEntity.ok(authService.register(request));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthenticationResponse> login(
                @RequestBody Users request
        ) {
            return ResponseEntity.ok(authService.authenticate(request));
        }
    

}

