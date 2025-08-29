package to.co.divinesolutions.tenors.property.service;

import to.co.divinesolutions.tenors.entity.UnitSection;
import to.co.divinesolutions.tenors.property.dto.AvailableSectionDto;
import to.co.divinesolutions.tenors.property.dto.UnitSectionDto;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface UnitSectionService {
    Response<UnitSection> save(UnitSectionDto dto);

    Optional<UnitSection> getOptionalByUid(String uid);

    Response<UnitSection> getSectionByUid(String uid);

    Response<UnitSection> deleteSection(String uid);

    List<UnitSectionDto> listAllSections();

    List<UnitSectionDto> listAllSectionsByUnitUid(String unitUid);

    List<AvailableSectionDto> myAvailableUnitSections(String userUid);

    void changeAvailability(UnitSection unitSection);
}
