package to.co.divinesolutions.tenors.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.entity.PropertyUnit;
import to.co.divinesolutions.tenors.entity.Unit;
import to.co.divinesolutions.tenors.property.dto.PropertyUnitDto;
import to.co.divinesolutions.tenors.property.repository.PropertyUnitRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyUnitServiceImpl implements PropertyUnitService{

    private final PropertyUnitRepository propertyUnitRepository;
    private final PropertyService propertyService;
    private final UnitService unitService;
    @Override
    public Response<PropertyUnit> save(PropertyUnitDto dto){
        try {
            Optional<Property> optionalProperty = propertyService.getOptionalByUid(dto.getPropertyUid());
            if (optionalProperty.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Error when saving property unit", null);
            }
            Property property = optionalProperty.get();

            Optional<Unit> optionalUnit = unitService.getOptionalByUid(dto.getPropertyUid());
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.INVALID_INPUT, "Error when saving property unit", null);
            }
            Unit unit = optionalUnit.get();

            Optional<PropertyUnit> propertyUnitOptional = getOptionalByUid(dto.getUid());

            if (dto.getUid() != null && !dto.getUid().isEmpty() && propertyUnitOptional.isEmpty()){
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Property unit could not be found or may have been deleted from the system", null);
            }

            PropertyUnit propertyUnit = propertyUnitOptional.orElse(new PropertyUnit());
            propertyUnit.setUnit(unit);
            propertyUnit.setProperty(property);
            PropertyUnit saved = propertyUnitRepository.save(propertyUnit);

            return new Response<>(true, ResponseCode.SUCCESS, "Property unit saved successfully", saved);
        }
        catch (Exception e){
            log.error("***** Error on saving property unit: {}",e.getMessage());
            return new Response<>(false, ResponseCode.INVALID_INPUT, "Error when saving property unit", null);
        }
    }

    public Optional<PropertyUnit> getOptionalByUid(String  uid){
        return uid != null && !uid.isEmpty() ? propertyUnitRepository.findFirstByUid(uid) : Optional.empty();
    }
    @Override
    public List<PropertyUnit> propertyUnits(){
        return propertyUnitRepository.findAll();
    }

    @Override
    public Response<PropertyUnit> findByUid(String uid){
        try {
            Optional<PropertyUnit> optionalPropertyUnit = getOptionalByUid(uid);
            return optionalPropertyUnit.map(propertyUnit -> new Response<>(true, ResponseCode.SUCCESS, "Success", propertyUnit)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Property unit could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            log.error("***** Error on fetching Property Unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when fetching property unit data", null);
        }
    }

    @Override
    public Response<PropertyUnit> delete(String uid){
        try {
            Optional<PropertyUnit> optionalUnit = getOptionalByUid(uid);
            if (optionalUnit.isEmpty()){
                return new Response<>(false, ResponseCode.DUPLICATE, "Property unit could not be found or may have been deleted from the system", null);
            }
            propertyUnitRepository.delete(optionalUnit.get());
            return new Response<>(true, ResponseCode.SUCCESS, "Property Unit deleted successfully", null);
        }
        catch (Exception e){
            log.error("***** Error on fetching property unit: {}",e.getMessage());
            return new Response<>(true, ResponseCode.INTERNAL_SERVER_ERROR, "Error when deleting property unit", null);
        }
    }
}
