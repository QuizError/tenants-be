package to.co.divinesolutions.tenors.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.UnitSection;
import to.co.divinesolutions.tenors.property.dto.AvailableSectionDto;
import to.co.divinesolutions.tenors.property.dto.UnitSectionDto;
import to.co.divinesolutions.tenors.property.service.UnitSectionService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("unit-sections")
public class UnitSectionController {

    @Autowired
    private UnitSectionService unitSectionService;

    @PostMapping
    public Response<UnitSection> save(@RequestBody UnitSectionDto dto){
        return unitSectionService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<UnitSection> getSectionByUid(@PathVariable String uid){
        return unitSectionService.getSectionByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<UnitSection> delete(@PathVariable String uid){
        return unitSectionService.deleteSection(uid);
    }

    @GetMapping
    public List<UnitSectionDto> units(){
        return unitSectionService.listAllSections();
    }

    @GetMapping("/unit/{uid}")
    public List<UnitSectionDto> listAllSectionsByUnitUid(@PathVariable String uid){
        return unitSectionService.listAllSectionsByUnitUid(uid);
    }

    @GetMapping("/available-units/{uid}")
    public List<AvailableSectionDto> myAvailableUnitSections(@PathVariable String uid){
        return unitSectionService.myAvailableUnitSections(uid);
    }

}
