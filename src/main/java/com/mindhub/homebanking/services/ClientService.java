package com.mindhub.homebanking.services;


import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<ClientDTO> getClientsDTO();




    Client findByEmail(String email);

    ClientDTO getClientDTO (String email);

    ClientDTO findById(long id);

    void saveClient(Client client);
}
