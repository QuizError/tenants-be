package to.co.divinesolutions.tenors.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.entity.Unit;
import to.co.divinesolutions.tenors.property.dto.UnitDto;
import to.co.divinesolutions.tenors.property.repository.UnitRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService{

    private final PropertyService propertyService;
    private final UnitRepository unitRepository;

    @Override
    public Response<Unit> save(UnitDto dto){
        try {

            Optional<Property> optionalProperty = propertyService.getOptionalByUid(dto.getPropertyUid());
            if (optionalProperty.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Property could not be found or may have been deleted from the system", null);
            }
            Property property = optionalProperty.get();

            Optional<Unit> optionalUnit = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Unit could not be found or may have been deleted from the system", null);
            }

            Unit unit = optionalUnit.orElse(new Unit());
            unit.setProperty(property);
            unit.setName(dto.getName());
            unit.setDescriptions(dto.getDescriptions());
            Unit saved = unitRepository.save(unit);

            return new Response<>(true, ResponseCode.SUCCESS, "Unit saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INVALID_INPUT, "Error when saving unit", null);
        }
    }
    @Override
    public Optional<Unit> getOptionalByUid(String  uid){
        return uid != null && !uid.isEmpty() ? unitRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<Unit> findByUid(String uid){
        try {
            Optional<Unit> optionalUnit = getOptionalByUid(uid);
            return optionalUnit.map(unit -> new Response<>(true, ResponseCode.SUCCESS, "Success", unit)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Unit could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching unit data", null);
        }
    }

    @Override
    public Response<Unit> delete(String uid){
        try {
            Optional<Unit> optionalUnit = getOptionalByUid(uid);
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Unit could not be found or may have been deleted from the system", null);
            }
            unitRepository.delete(optionalUnit.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Unit deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting unit data", null);
        }
    }

    @Override
    public List<Unit> units(){
        return unitRepository.findAll();
    }

    @Override
    public List<UnitDto> propertyUnits(String propertyUid){
        try {
            Optional<Property> optionalProperty = propertyService.getOptionalByUid(propertyUid);
            if (optionalProperty.isEmpty()){
                log.error("Property with uid: {} could not be found", propertyUid);
                return Collections.emptyList();
            }
            Property property = optionalProperty.get();
            List<UnitDto> unitDtoList = new ArrayList<>();
            for (Unit unit : unitRepository.findAllByProperty(property)) {
                UnitDto dto = new UnitDto();
                dto.setName(unit.getName());
                dto.setUid(unit.getUid());
                dto.setPropertyUid(unit.getProperty() != null ? unit.getProperty().getUid() : null);
                dto.setDescriptions(unit.getDescriptions());
                unitDtoList.add(dto);
            }

            return unitDtoList;
        }
        catch (Exception e){
            return Collections.emptyList();
        }
    }
}
