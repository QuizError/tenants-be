package to.co.divinesolutions.tenors.property.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import to.co.divinesolutions.tenors.enums.Rooms;

@Getter
@Setter
@NoArgsConstructor
public class RoomDto {
    private Integer count;
    private Rooms room;
}
