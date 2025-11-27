package to.co.divinesolutions.tenors.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.Notice;
import to.co.divinesolutions.tenors.entity.Rental;

import java.util.List;
import java.util.Optional;
@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Optional<Notice> findFirstByUid(String uid);
    List<Notice> findAllByRental(Rental rental);
    List<Notice> findAllByPropertyId(Long id);
}
