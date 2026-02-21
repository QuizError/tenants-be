package to.co.divinesolutions.tenors.uaa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findFirstByUid(String uid);
    @Query("SELECT u FROM User u WHERE u.active = true")
    Page<User> findActiveUsers(Pageable pageable);
    Optional<User> findFirstByEmailAndPassword(String email,String password);

    @Query("SELECT u.msisdn FROM User u join Client c on u.id = c.user.id join Rental r on c.id =r.client.id where r.propertyId =:propertyId and r.rentalStatus ='ACTIVE'")
    List<String> findAllTenantsMsisdnOfProperty(@Param("propertyId") Long propertyId);
}
