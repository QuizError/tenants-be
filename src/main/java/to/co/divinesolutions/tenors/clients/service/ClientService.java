package to.co.divinesolutions.tenors.clients.service;

import to.co.divinesolutions.tenors.clients.dto.ClientDto;
import to.co.divinesolutions.tenors.entity.Client;
import to.co.divinesolutions.tenors.utils.Response;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Response<Client> save(ClientDto dto);

    Optional<Client> getOptionalByUid(String uid);

    Response<Client> findByUid(String uid);

    Response<Client> delete(String uid);

    List<Client> listAllClients();

    List<Client> listAllMyClients(String userUid);
}
