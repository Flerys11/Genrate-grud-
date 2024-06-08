package template.generation.service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import template.generation.model.UsersRepository;


@Service
public class UserDetailsServiceImp implements UserDetailsService
{
	 private final UsersRepository repository; 

	 public UserDetailsServiceImp(UsersRepository repository ) { 
		this.repository = repository; 
 	 } 


	@Override
	 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return repository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	 }



}

