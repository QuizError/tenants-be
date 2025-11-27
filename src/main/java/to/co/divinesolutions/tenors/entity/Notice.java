package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.NoticeType;
import to.co.divinesolutions.tenors.utils.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notices")
public class Notice extends BaseEntity {
    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String details;

    private String filePath;
    private String sentTo;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    @ManyToOne
    private Rental rental;

    private Long clientId;
    private Long propertyId;
    private Long unitSectionId;
}
