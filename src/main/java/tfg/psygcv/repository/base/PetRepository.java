package tfg.psygcv.repository.base;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tfg.psygcv.model.pet.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

  @Query("SELECT p FROM Pet p WHERE p.id = :petId AND p.active = true")
  Optional<Pet> findByIdAndActive(@Param("petId") Long petId);

  @Query("SELECT p FROM Pet p WHERE p.owner.id = :ownerId AND p.active = TRUE")
  List<Pet> findByOwnerId(@Param("ownerId") Long ownerId);
}
