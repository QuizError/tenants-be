package to.co.divinesolutions.tenors.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.Unit;
import to.co.divinesolutions.tenors.property.dto.UnitDto;
import to.co.divinesolutions.tenors.property.service.UnitService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("units")
public class UnitController {

    @Autowired
    private UnitService unitService;

    @PostMapping
    public Response<Unit> save(@RequestBody UnitDto dto){
        return unitService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<Unit> findByUid(@PathVariable String uid){
        return unitService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Unit> delete(@PathVariable String uid){
        return unitService.delete(uid);
    }

    @GetMapping
    public List<Unit> units(){
        return unitService.units();
    }

    @GetMapping("/property/{uid}")
    public List<UnitDto> propertyUnits(@PathVariable String uid){
        return unitService.propertyUnits(uid);
    }
}
