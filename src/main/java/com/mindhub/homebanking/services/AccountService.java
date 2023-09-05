package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccounts();

    AccountDTO findById(long id);

    Account findByNumber(String number);

    void addAccount (Account account);

    Account findByNumberAndClient(String number, Client client);


}
