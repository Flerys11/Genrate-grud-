package template.generation.repository;
import template.generation.model.employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<employe, Integer>
{


}

