package to.co.divinesolutions.tenors.checklist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import to.co.divinesolutions.tenors.checklist.service.ChecklistInstanceService;
import to.co.divinesolutions.tenors.entity.ChecklistInstance;

import java.util.List;

@RestController
@RequestMapping("checklist-instances")
@RequiredArgsConstructor
public class ChecklistInstanceController {

    private final ChecklistInstanceService checklistInstanceService;

    @GetMapping("template/{uid}")
    public List<ChecklistInstance> getChecklistByTemplate(@PathVariable String uid){
        return checklistInstanceService.getChecklistByTemplate(uid);
    }
}
