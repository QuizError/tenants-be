package to.co.divinesolutions.tenors.property.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import to.co.divinesolutions.tenors.entity.*;
import to.co.divinesolutions.tenors.property.dto.AvailableSectionDto;
import to.co.divinesolutions.tenors.property.dto.RoomDto;
import to.co.divinesolutions.tenors.property.dto.UnitSectionDto;
import to.co.divinesolutions.tenors.property.repository.UnitSectionDefinitionRepository;
import to.co.divinesolutions.tenors.property.repository.UnitSectionRepository;
import to.co.divinesolutions.tenors.utils.Response;
import to.co.divinesolutions.tenors.utils.ResponseCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UnitSectionServiceImpl implements UnitSectionService{

    private final UnitSectionDefinitionRepository unitSectionDefinitionRepository;
    private final UnitSectionRepository unitSectionRepository;
    private final PropertyService propertyService;
    private final UnitService unitService;

    @Override
    @Transactional
    public Response<UnitSection> save(UnitSectionDto dto){
        log.info("************** Unit Section DTO: {}",dto);
        Optional<Unit> optionalUnit= unitService.getOptionalByUid(dto.getUnitUid());
        if (optionalUnit.isEmpty()){
            return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Unit could not be found or may have been deleted from the system",null);
        }
        Unit unit = optionalUnit.get();

        Optional<UnitSection> optionalUnitSection = getOptionalByUid(dto.getUid());
        if (dto.getUid() != null && !dto.getUid().isEmpty() && optionalUnitSection.isEmpty()) {
            return new Response<>(false, ResponseCode.NO_DATA_FOUND,"Unit section could not be found or may have been deleted from the system",null);
        }
        UnitSection unitSection = optionalUnitSection.orElse(new UnitSection());
        unitSection.setUnit(unit);
        unitSection.setPropertyId(unit.getProperty().getId());
        unitSection.setPrice(dto.getPrice());
        unitSection.setCurrency(dto.getCurrency());
        unitSection.setName(dto.getName());
        unitSection.setGasMeter(dto.getGasMeter());
        unitSection.setWaterMeter(dto.getWaterMeter());
        unitSection.setElectricityMeter(dto.getElectricityMeter());
        unitSection.setAvailable(dto.getAvailable());
        UnitSection savedSection = unitSectionRepository.save(unitSection);

        if (unitSectionDefinitionRepository.existsByUnitSection(savedSection)){
            unitSectionDefinitionRepository.deleteAllByUnitSection(savedSection);
            unitSectionDefinitionRepository.flush();
        }
        for (RoomDto room : dto.getRoomDtos()){
            UnitSectionDefinition definition  = new UnitSectionDefinition();
            definition.setRoom(room.getRoom());
            definition.setCount(room.getCount());
            definition.setUnitSection(savedSection);
            unitSectionDefinitionRepository.save(definition);
        }

        return new Response<>(true,ResponseCode.SUCCESS, "Unit Section saved successfully",savedSection );
    }

    @Override
    public Optional<UnitSection> getOptionalByUid(String uid){
        return uid != null && !uid.isEmpty() ? unitSectionRepository.findFirstByUid(uid) : Optional.empty();
    }

    @Override
    public Response<UnitSection> getSectionByUid(String uid) {
        try {
            Optional<UnitSection> optionalUnitSection = getOptionalByUid(uid);
            return optionalUnitSection.map(unitSection -> new Response<>(true, ResponseCode.SUCCESS, "Success", unitSection)).orElseGet(() -> new Response<>(false, ResponseCode.NO_DATA_FOUND, "Unit section could not be found or may have been deleted from the system", null));
        }
        catch (Exception e){
            return new Response<>(false, ResponseCode.INTERNAL_SERVER_ERROR,"An error occurred during fetching section", null);
        }
    }

    @Override
    public Response<UnitSection> deleteSection(String uid) {
        try {
            Optional<UnitSection> optionalUnitSection = getOptionalByUid(uid);
            if (optionalUnitSection.isEmpty()) {
                return new Response<>(false, ResponseCode.NO_DATA_FOUND, "Unit section could not be found or may have been deleted from the system", null);
            }
            else {
                UnitSection unitSection = optionalUnitSection.get();
                unitSectionDefinitionRepository.deleteAllByUnitSection(unitSection);
                unitSectionDefinitionRepository.flush();
                unitSectionRepository.delete(unitSection);
                return new Response<>(true, ResponseCode.SUCCESS,"Section deleted successfully", null);
            }
        }
        catch (Exception e){
            return new Response<>(false, ResponseCode.INTERNAL_SERVER_ERROR,"An error occurred during deleting section", null);
        }
    }

    @Override
    public List<UnitSectionDto> listAllSections(){

        if (!unitSectionRepository.findAll().isEmpty()){
            log.info("Items: {}", unitSectionRepository.findAll().size());
            List<UnitSectionDto> unitSectionDtoList = new ArrayList<>();
            List<RoomDto> roomDtoList = new ArrayList<>();
            for (UnitSection unitSection: unitSectionRepository.findAll()){
                UnitSectionDto unitSectionDto = new UnitSectionDto();
                unitSectionDto.setName(unitSection.getName());
                unitSectionDto.setUid(unitSection.getUid());
                unitSectionDto.setPrice(unitSection.getPrice());
                unitSectionDto.setAvailable(unitSection.getAvailable());
                unitSectionDto.setCurrency(unitSection.getCurrency());
                unitSectionDto.setUnitUid(unitSection.getUnit().getUid());
                unitSectionDto.setUnitName(unitSection.getUnit().getName());
                unitSectionDto.setGasMeter(unitSection.getGasMeter());
                unitSectionDto.setWaterMeter(unitSection.getWaterMeter());
                unitSectionDto.setElectricityMeter(unitSection.getElectricityMeter());
                for (UnitSectionDefinition definition: unitSection.getDefinitions()){
                    RoomDto roomDto = new RoomDto();
                    roomDto.setCount(definition.getCount());
                    roomDto.setRoom(definition.getRoom());
                    roomDtoList.add(roomDto);
                }
                unitSectionDto.setRoomDtos(roomDtoList);
                unitSectionDtoList.add(unitSectionDto);
            }
            return unitSectionDtoList;
        }
        else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<UnitSectionDto> listAllSectionsByUnitUid(String unitUid) {
        Optional<Unit> optionalUnit = unitService.getOptionalByUid(unitUid);
        if (optionalUnit.isEmpty() || unitSectionRepository.findAllByUnit(optionalUnit.get()).isEmpty()) {
            return Collections.emptyList();
        } else {
            List<UnitSectionDto> unitSectionList = new ArrayList<>();

            for (UnitSection unitSection: unitSectionRepository.findAllByUnit(optionalUnit.get())) {
                UnitSectionDto unitSectionDto = new UnitSectionDto();
                unitSectionDto.setAvailable(unitSection.getAvailable());
                unitSectionDto.setUnitName(unitSection.getUnit().getName());
                unitSectionDto.setName(unitSection.getName());
                unitSectionDto.setUid(unitSection.getUid());
                unitSectionDto.setPrice(unitSection.getPrice());
                unitSectionDto.setGasMeter(unitSection.getGasMeter());
                unitSectionDto.setWaterMeter(unitSection.getWaterMeter());
                unitSectionDto.setElectricityMeter(unitSection.getElectricityMeter());
                unitSectionDto.setUnitUid(unitSection.getUnit().getUid());
                unitSectionDto.setCurrency(unitSection.getCurrency());

                // Create a fresh list for each section
                List<RoomDto> roomDtoList = new ArrayList<>();
                for (UnitSectionDefinition definition: unitSection.getDefinitions()) {
                    RoomDto roomDto = new RoomDto();
                    roomDto.setCount(definition.getCount());
                    roomDto.setRoom(definition.getRoom());
                    roomDtoList.add(roomDto);
                }

                unitSectionDto.setRoomDtos(roomDtoList);
                unitSectionList.add(unitSectionDto);
            }

            return unitSectionList;
        }
    }

    @Override
    public List<AvailableSectionDto> myAvailableUnitSections(String userUid){
        List<Property> properties = propertyService.getMyProperties(userUid);
        if (!properties.isEmpty()){
            List<Long> propertyIds = new ArrayList<>();
            for (Property property: properties){
                propertyIds.add(property.getId());
            }
            List<AvailableSectionDto> availableSectionDtoList = new ArrayList<>();
            List<UnitSection> unitSections = unitSectionRepository.findAllByAvailableTrueAndPropertyIdIn(propertyIds);
            if (!unitSections.isEmpty()){
                for (UnitSection unitSection :  unitSections){
                    AvailableSectionDto sectionDto  = new AvailableSectionDto();
                    sectionDto.setAvailability(unitSection.getAvailable());
                    sectionDto.setPrice(unitSection.getPrice());
                    sectionDto.setName(unitSection.getName());
                    sectionDto.setPropertyName(unitSection.getUnit().getProperty().getName());
                    sectionDto.setLocation(unitSection.getUnit().getProperty().getLocation());
                    sectionDto.setCurrency(unitSection.getCurrency());
                    sectionDto.setUid(unitSection.getUid());
                    sectionDto.setUnitName(unitSection.getUnit().getName());
                    availableSectionDtoList.add(sectionDto);
                }
            }
            return availableSectionDtoList;
        }
        return Collections.emptyList();
    }

    @Override
    public void changeAvailability(UnitSection unitSection){
        unitSectionRepository.save(unitSection);
    }

}
