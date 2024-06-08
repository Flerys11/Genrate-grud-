package com.example.authet.repository;

import com.example.authet.model.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PosteRepository extends JpaRepository<Poste, Integer>
{
}

