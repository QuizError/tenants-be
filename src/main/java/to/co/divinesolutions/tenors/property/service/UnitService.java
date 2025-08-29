package to.co.divinesolutions.tenors.property.service;

import to.co.divinesolutions.tenors.entity.Unit;
import to.co.divinesolutions.tenors.property.dto.UnitDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface UnitService {
    Response<Unit> save(UnitDto dto);

    Optional<Unit> getOptionalByUid(String uid);

    Response<Unit> findByUid(String uid);

    Response<Unit> delete(String uid);

    List<Unit> units();

    List<UnitDto> propertyUnits(String propertyUid);
}
