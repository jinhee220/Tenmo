package com.techelevator.tenmo.services;

// RestTemplate used here from Unit 11 & 12

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    // set token
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;

    }



    // ------------------------------------------------------- //
    // ------------------- ACCOUNT SERVICE ------------------- //
    // ------------------------------------------------------- //

    public Account getAccount(int userId){
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "account/" + userId,
                            HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


    public void updateAccount(Account account) {
        try {
            //put new account by accountId
            restTemplate.put(baseUrl + "accounts", makeAccountEntity(account));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }



    // ------------------------------------------------------- //
    // -------------------- USER SERVICE --------------------- //
    // ------------------------------------------------------- //

    public User[] getUsers () {
        User[] users  = null;
        try {
            ResponseEntity <User[] > userslist = restTemplate.exchange(baseUrl + "users", HttpMethod.GET,
                    makeAuthEntity( ), User[].class);
            users = userslist.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public User getUserByAccountId(int accountId) {
        User result = new User();
        try {
            ResponseEntity<User> response = restTemplate.exchange(baseUrl + "users/account/" + accountId, HttpMethod.GET, makeAuthEntity(), User.class);
            result = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return result;
    }


    // ------------------------------------------------------- //
    // ------------------ TRANSFER SERVICE ------------------- //
    // ------------------------------------------------------- //


    public Boolean createTransfer(Transfer newTransfer) {
        Boolean success = false;
        String url = baseUrl + "transfers";
        try {
           /// ResponseEntity<Transfer> response = restTemplate.postForEntity(url, newTransfer, Transfer.class);

            ResponseEntity<Transfer> response = restTemplate.exchange (url, HttpMethod.POST, makeTransferEntity( newTransfer), Transfer.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                success = true;
            } else {
                throw new RuntimeException("Failed to create transfer. Status code: " + response.getStatusCode());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }


    public Transfer[] getTransfersList(int userId) {
          Transfer[] result = null;
          try {
              String url = baseUrl + "transfers/user/" + userId;
              ResponseEntity<Transfer[]> response = restTemplate.getForEntity(url, Transfer[].class);

              if (response.getStatusCode() == HttpStatus.OK) {
                  result = response.getBody();
              } else {
                  throw new RuntimeException("Failed to fetch transfer list. Status code: " + response.getStatusCode());
              }
          } catch (RestClientResponseException | ResourceAccessException e) {
              BasicLogger.log(e.getMessage());
          }
          return result;
      }


    public Transfer getTransferById(int transferId) {
        String url = baseUrl + "transfers/" + transferId;
        ResponseEntity<Transfer> response = restTemplate.getForEntity(url, Transfer.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch transfer details. Status code: " + response.getStatusCode());
        }
    }


    public List<Transfer> getPendingTransfersList(int userId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        String url = baseUrl + "transfers/pending/" + userId;
        try {
            ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                pendingTransfers = response.getBody();
            } else {
                throw new RuntimeException("Failed to fetch pending transfers. Status code: " + response.getStatusCode());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return pendingTransfers;
    }


    public void updateTransfer(Transfer transfer) {
        try {
            //update new transfer
            restTemplate.put(baseUrl + "transfers", makeTransferEntity(transfer));
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }



    // ------------------------------------------------------- //
    // ------------------ SERVICE UTILITY -------------------- //
    // ------------------------------------------------------- //

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

}
