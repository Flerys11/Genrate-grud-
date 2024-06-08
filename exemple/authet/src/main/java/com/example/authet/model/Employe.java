package com.example.authet.model;
import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

@Entity
public class Employe 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nom;
	private String prenom;
	private Date date_naissance;
	 @ManyToOne
	 @JoinColumn(name = "idposte", nullable = false)
	private Poste poste;

	public void setId( int id )
	{
		this.id=id; 
	}

	public void setNom( String nom )
	{
		this.nom=nom; 
	}

	public void setPrenom( String prenom )
	{
		this.prenom=prenom; 
	}

	public void setDate_naissance( Date date_naissance )
	{
		this.date_naissance=date_naissance; 
	}

	public void setPoste( Poste poste )
	{
		this.poste=poste; 
	}


	public int getId()
	{
		return this.id; 
	}

	public String getNom()
	{
		return this.nom; 
	}

	public String getPrenom()
	{
		return this.prenom; 
	}

	public Date getDate_naissance()
	{
		return this.date_naissance; 
	}

	public Poste getPoste()
	{
		return poste; 
	}


	public Employe(int id, String nom, String prenom, Date date_naissance, Poste poste )
	{
		this.setId(id); 
		this.setNom(nom); 
		this.setPrenom(prenom); 
		this.setDate_naissance(date_naissance); 
		this.setPoste(poste); 
	}
	public Employe()
	{

	}
}

