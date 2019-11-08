package com.pkp.repository;
import com.pkp.domain.World;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the World entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorldRepository extends JpaRepository<World, Long> {

}
