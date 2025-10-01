package to.co.divinesolutions.tenors.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import to.co.divinesolutions.tenors.entity.ChecklistTemplate;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.enums.ChecklistType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistTemplateRepository extends JpaRepository<ChecklistTemplate,Long> {
    Optional<ChecklistTemplate> findFirstByUid(String uid);
    Optional<ChecklistTemplate>findFirstByPropertyAndChecklistTypeAndActiveTrue(Property property, ChecklistType checklistType);
    List<ChecklistTemplate>findAllByProperty(Property property);
}
