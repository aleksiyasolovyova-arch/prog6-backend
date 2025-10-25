package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "owners")
public class OwnerProjectionJpaEntity {

    @Id
    private UUID id;  // ← ADD THIS

    private String email;
    private String passwordHash;

    // Constructors, getters, setters...

    protected OwnerProjectionJpaEntity() {}

    public OwnerProjectionJpaEntity(UUID id, String email, String passwordHash) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
}
