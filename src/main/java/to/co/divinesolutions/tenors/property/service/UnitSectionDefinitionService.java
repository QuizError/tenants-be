package to.co.divinesolutions.tenors.property.service;

import to.co.divinesolutions.tenors.entity.UnitSectionDefinition;

import java.util.Optional;

public interface UnitSectionDefinitionService {
    Optional<UnitSectionDefinition> getOptionalByUid(String uid);
}
