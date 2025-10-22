package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Owner;
import kdg.be.prog6.kdg.restaurant.domain.OwnerId;

import java.util.Optional;

public interface OwnerJPARepository extends JpaRepository<>{
    Optional<Owner> findById(OwnerId ownerId);
    void save(Owner owner);
}
