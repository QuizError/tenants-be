package to.co.divinesolutions.tenors.property.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.entity.Property;
import to.co.divinesolutions.tenors.property.dto.PropertyDto;
import to.co.divinesolutions.tenors.property.service.PropertyService;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("properties")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

    @PostMapping
    public Response<Property> save(@RequestBody PropertyDto dto){
        return propertyService.save(dto);
    }

    @GetMapping("{uid}")
    public Response<Property> save(@PathVariable String uid){
        return propertyService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Property> delete(@PathVariable String uid){
        return propertyService.delete(uid);
    }

    @GetMapping
    public List<Property> userList(){
        return propertyService.properties();
    }

    @GetMapping("/user/{uid}")
    public List<Property> myProperties(@PathVariable String uid){
        return propertyService.getMyProperties(uid);
    }
}
