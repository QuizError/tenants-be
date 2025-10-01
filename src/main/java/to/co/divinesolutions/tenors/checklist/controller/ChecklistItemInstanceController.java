package to.co.divinesolutions.tenors.checklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.checklist.dto.ChecklistItemInstanceDto;
import to.co.divinesolutions.tenors.checklist.service.ChecklistItemInstanceService;
import to.co.divinesolutions.tenors.entity.ChecklistItemInstance;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("checklist-item-instances")
@RequiredArgsConstructor
public class ChecklistItemInstanceController {

    @Autowired
    private ChecklistItemInstanceService checklistItemInstanceService;

    @PostMapping
    public Response<ChecklistItemInstance> save(@RequestBody ChecklistItemInstanceDto dto){
        return checklistItemInstanceService.save(dto);
    }

    @GetMapping("/instance/{uid}")
    public List<ChecklistItemInstance> checklistItemInstanceList(@PathVariable String uid){
        return checklistItemInstanceService.checklistItemInstanceList(uid);
    }

    @GetMapping("{uid}")
    public Response<ChecklistItemInstance> findByUid(@PathVariable String uid){
        return checklistItemInstanceService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<ChecklistItemInstance> delete(@PathVariable String uid){
        return checklistItemInstanceService.delete(uid);
    }
}
