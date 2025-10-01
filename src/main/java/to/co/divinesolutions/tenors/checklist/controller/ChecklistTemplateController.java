package to.co.divinesolutions.tenors.checklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.checklist.dto.ChecklistTemplateDto;
import to.co.divinesolutions.tenors.checklist.service.ChecklistTemplateService;
import to.co.divinesolutions.tenors.entity.ChecklistTemplate;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("checklist-templates")
@RequiredArgsConstructor
public class ChecklistTemplateController {

    @Autowired
    private ChecklistTemplateService  checklistTemplateService;

    @PostMapping
    public Response<ChecklistTemplate> save(@RequestBody ChecklistTemplateDto dto){
        return checklistTemplateService.save(dto);
    }

    @GetMapping
    public List<ChecklistTemplate> allClients(){
        return checklistTemplateService.checklistTemplates();
    }

    @GetMapping("/property/{uid}")
    public List<ChecklistTemplate> allMyClients(@PathVariable String uid){
        return checklistTemplateService.listAllMyClients(uid);
    }

    @GetMapping("{uid}")
    public Response<ChecklistTemplate> findByUid(@PathVariable String uid){
        return checklistTemplateService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<ChecklistTemplate> delete(@PathVariable String uid){
        return checklistTemplateService.delete(uid);
    }
}
