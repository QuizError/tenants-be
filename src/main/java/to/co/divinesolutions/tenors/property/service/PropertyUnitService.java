package to.co.divinesolutions.tenors.property.service;

import to.co.divinesolutions.tenors.entity.PropertyUnit;
import to.co.divinesolutions.tenors.property.dto.PropertyDto;
import to.co.divinesolutions.tenors.property.dto.PropertyUnitDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

public interface PropertyUnitService {

    Response<PropertyUnit> save(PropertyUnitDto dto);

    List<PropertyUnit> propertyUnits();

    Response<PropertyUnit> findByUid(String uid);

    Response<PropertyUnit> delete(String uid);
}
