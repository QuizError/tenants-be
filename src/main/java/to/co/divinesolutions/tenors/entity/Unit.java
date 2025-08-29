package to.co.divinesolutions.tenors.entity;

import jakarta.persistence.*;
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
@Table(name = "units")
public class Unit  extends BaseEntity {
    private String name;
    private String descriptions;

    @ManyToOne
    private Property property;

    //    private Integer publicWashrooms;
//    private Integer sittingRooms;
//    private Integer bedRooms;
//    private Integer kitchens;
//    private Integer masterBedrooms;

}
