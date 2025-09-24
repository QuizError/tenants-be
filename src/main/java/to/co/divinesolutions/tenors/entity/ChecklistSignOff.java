package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.utils.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "checlist_sign_offs")
public class ChecklistSignOff extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "checklist_instance_id")
    private ChecklistInstance checklistInstance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    private Boolean clientSigned = false;
    private Boolean managerSigned = false;
}
