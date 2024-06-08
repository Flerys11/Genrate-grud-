package template.generation.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.persistence.fff;

@Entity
##TABLE##
public class Poste 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom;

	public void setId( int id )
	{
		this.id=id; 
	}

	public void setNom( String nom )
	{
		this.nom=nom; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getNom()
	{
		return this.nom; 
	}



	public Poste(int id, String nom )
	{
		this.setId(id); 
		this.setNom(nom); 
	}
	public Poste()
	{

	}
}

