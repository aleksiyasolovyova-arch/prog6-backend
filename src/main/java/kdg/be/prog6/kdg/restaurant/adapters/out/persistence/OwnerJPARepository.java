package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnerJPARepository extends JpaRepository<OwnerProjectionJpaEntity, UUID> {
    Optional<OwnerProjectionJpaEntity> findById(UUID ownerId);
    void save(Owner owner);
}
