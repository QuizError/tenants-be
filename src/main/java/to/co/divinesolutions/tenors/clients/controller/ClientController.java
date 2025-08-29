package to.co.divinesolutions.tenors.clients.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import to.co.divinesolutions.tenors.clients.dto.ClientDto;
import to.co.divinesolutions.tenors.clients.service.ClientService;
import to.co.divinesolutions.tenors.entity.Client;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public Response<Client> save(@RequestBody ClientDto dto){
        return clientService.save(dto);
    }

    @GetMapping
    public List<Client> allClients(){
        return clientService.listAllClients();
    }

    @GetMapping("/owner/{uid}")
    public List<Client> allMyClients(@PathVariable String uid){
        return clientService.listAllMyClients(uid);
    }

    @GetMapping("{uid}")
    public Response<Client> findByUid(@PathVariable String uid){
        return clientService.findByUid(uid);
    }

    @DeleteMapping("{uid}")
    public Response<Client> delete(@PathVariable String uid){
        return clientService.delete(uid);
    }
}
