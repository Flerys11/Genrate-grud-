package template.generation.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

@Entity
##TABLE##
public class Token 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String token;
	private boolean is_logged_out;
	 @ManyToOne
	 @JoinColumn(name = "user_id", nullable = false)
	private Users users;

	public void setId( int id )
	{
		this.id=id; 
	}

	public void setToken( String token )
	{
		this.token=token; 
	}

	public void setIs_logged_out( boolean is_logged_out )
	{
		this.is_logged_out=is_logged_out; 
	}

	public void setUsers( Users users )
	{
		this.users=users; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getToken()
	{
		return this.token; 
	}

	public boolean getIs_logged_out()
	{
		return this.is_logged_out; 
	}

	public Users getUsers()
	{
		return users; 
	}



	public Token(int id, String token, boolean is_logged_out, Users users )
	{
		this.setId(id); 
		this.setToken(token); 
		this.setIs_logged_out(is_logged_out); 
		this.setUsers(users); 
	}
	public Token()
	{

	}
}

