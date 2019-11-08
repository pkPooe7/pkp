package com.pkp.repository;
import com.pkp.domain.Alien;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Alien entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlienRepository extends JpaRepository<Alien, Long> {

}
