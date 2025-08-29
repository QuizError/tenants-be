package to.co.divinesolutions.tenors.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.UnitSectionDefinition;
import to.co.divinesolutions.tenors.property.repository.UnitSectionDefinitionRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitSectionDefinitionServiceImpl implements UnitSectionDefinitionService{

    private final UnitSectionDefinitionRepository unitSectionDefinitionRepository;

    @Override
    public Optional<UnitSectionDefinition> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? unitSectionDefinitionRepository.findFirstByUid(uid) : Optional.empty();
    }
}
