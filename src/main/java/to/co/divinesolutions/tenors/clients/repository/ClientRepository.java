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
            SELECT * FROM clients WHERE created_by IN (SELECT user_id FROM group_ownership_members WHERE user_id=:userId)
            """,
            nativeQuery = true)
    List<Client> getMyClients(Long userId);
}
