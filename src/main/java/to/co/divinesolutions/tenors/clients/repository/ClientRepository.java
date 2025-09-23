package to.co.divinesolutions.tenors.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findFirstByUid(String uid);
    @Query(value = """
            SELECT DISTINCT c.* FROM clients c JOIN rentals r ON r.client_id = c.id JOIN properties p ON p.id = r.property_id
            JOIN group_ownership_members gom ON gom.group_id = p.owner_id WHERE gom.user_id =:userId
            """,
            nativeQuery = true)
    List<Client> getMyClients(Long userId);
}
