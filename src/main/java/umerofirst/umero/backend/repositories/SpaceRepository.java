package umerofirst.umero.backend.repositories;

import umerofirst.umero.backend.models.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, String> {

    List<Space> findByIsActiveTrue();

    List<Space> findByCityIgnoreCase(String city);
}