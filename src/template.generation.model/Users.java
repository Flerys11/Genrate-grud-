package template.generation.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.persistence.fff;

@Entity
##TABLE##
public class Users 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String first_name;
	private String last_name;
	private String username;
	private String password;
	private String role;

	public void setId( int id )
	{
		this.id=id; 
	}

	public void setFirst_name( String first_name )
	{
		this.first_name=first_name; 
	}

	public void setLast_name( String last_name )
	{
		this.last_name=last_name; 
	}

	public void setUsername( String username )
	{
		this.username=username; 
	}

	public void setPassword( String password )
	{
		this.password=password; 
	}

	public void setRole( String role )
	{
		this.role=role; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getFirst_name()
	{
		return this.first_name; 
	}

	public String getLast_name()
	{
		return this.last_name; 
	}

	public String getUsername()
	{
		return this.username; 
	}

	public String getPassword()
	{
		return this.password; 
	}

	public String getRole()
	{
		return this.role; 
	}



	public Users(int id, String first_name, String last_name, String username, String password, String role )
	{
		this.setId(id); 
		this.setFirst_name(first_name); 
		this.setLast_name(last_name); 
		this.setUsername(username); 
		this.setPassword(password); 
		this.setRole(role); 
	}
	public Users()
	{

	}
}

