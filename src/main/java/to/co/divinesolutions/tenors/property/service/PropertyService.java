package to.co.divinesolutions.tenors.property.service;

import to.co.divinesolutions.tenors.entity.GroupOwnership;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.property.dto.PropertyDto;
import to.co.divinesolutions.tenors.uaa.dto.UserDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface PropertyService {
    Response<Property> save(PropertyDto dto);

    Optional<Property> getOptionalByUid(String uid);

    List<Property> properties();

    Response<Property> findByUid(String uid);

    Response<Property> delete(String uid);

    List<Property> getMyProperties(String userUid);

    List<Long> getMyPropertyIds(String userUid);
}
