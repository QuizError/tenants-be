package to.co.divinesolutions.tenors.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.PropertyUnit;
import to.co.divinesolutions.tenors.property.dto.PropertyUnitDto;
import to.co.divinesolutions.tenors.property.service.PropertyUnitService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("property-units")
public class PropertyUnitController {

    @Autowired
    private PropertyUnitService propertyUnitService;

    @PostMapping
    public Response<PropertyUnit> save(@RequestBody PropertyUnitDto dto){
        return propertyUnitService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<PropertyUnit> save(@PathVariable String uid){
        return propertyUnitService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<PropertyUnit> delete(@PathVariable String uid){
        return propertyUnitService.delete(uid);
    }

    @GetMapping
    public List<PropertyUnit> propertyUnits(){
        return propertyUnitService.propertyUnits();
    }
}
