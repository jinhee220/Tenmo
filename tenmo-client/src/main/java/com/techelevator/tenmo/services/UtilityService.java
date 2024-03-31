package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UtilityService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final TransferService transferService = new TransferService(API_BASE_URL);


    public List<User> printSendRequestUserList(User currentUser) {

        System.out.println("------------------------------------------- ");
        System.out.println("Users ");
        System.out.println("ID         Name");
        System.out.println("------------------------------------------- ");

        User testUser = currentUser;

        int currentUserAccountId = testUser.getId();
        String currentUserName = testUser.getUsername();
        List<User> availableUsersList = new ArrayList<>();
        for (User x : transferService.getUsers()) {
            if (currentUserAccountId != x.getId()) {
                System.out.println(String.format("%S       %S", x.getId(), x.getUsername()));
                availableUsersList.add(x);
            }
        }
        System.out.println("------------------------------------------- ");
        return availableUsersList;
    }

    public List<Integer> getAllUsers() {
        List<Integer> allUsers = new ArrayList<>();

        for (User x : transferService.getUsers()) {
            allUsers.add(x.getId());
        }
        return allUsers;
    }

    public List<Integer> getTransferIds(List<Transfer> transfers) {
        List<Integer> ids = new ArrayList<>();

        for (Transfer x : transfers) {
            ids.add(x.getTransferId());
        }
        return ids;
    }

    public List<Transfer> getAllTransfers(int userId) {
        List<Transfer> allTransfers = new ArrayList<>();

        for (Transfer x : transferService.getTransfersList(userId)) {
            allTransfers.add(x);
        }
        return allTransfers;
    }

    public Transfer getTransferByAccountId(int accountId, Transfer userTransfer) {
        List<Account> allAccounts = new ArrayList<>();
        List<Transfer> requesterTransfers = new ArrayList<>();
        Transfer requesterTransfer = new Transfer();
        int requesterId;
        Account requesterAccount;

        //search through all users and add each users account to a list
        for (User x : transferService.getUsers()) {
            allAccounts.add(transferService.getAccount(x.getId()));
        }

        //search through all accounts and find the one that matches input accountId
        for (Account y : allAccounts) {
            if(accountId == y.getAccountId()){
                requesterAccount = y;
                requesterId = requesterAccount.getUserId();
                requesterTransfers = getAllTransfers(requesterId);
            }
        }

        for (Transfer z: requesterTransfers) {
            if(z.getTransferStatusId() == userTransfer.getTransferStatusId() &&
                    z.getFromUserId() == userTransfer.getToUserId() &&
                    z.getToUserId() == userTransfer.getFromUserId() &&
                    z.getAmount() == userTransfer.getAmount()){
                requesterTransfer = z;
            }
        }

        return requesterTransfer;
    }

/*    public void viewTransferDetails(int transferId) {
        Transfer transfer = transferService.getTransferById(transferId);
        if (transfer != null) {
            System.out.println("--------------------------------------------");
            System.out.println("Transfer Details");
            System.out.println("--------------------------------------------");
            System.out.println("Id: " + transfer.getTransferId());
            System.out.println("From: " + getPartyName(transfer.getFromUserId()));
            System.out.println("To: " + getPartyName(transfer.getToUserId()));
            System.out.println("Type: " + getTransferType(transfer.getTransferTypeId()));
            System.out.println("Status: " + getTransferStatus(transfer.getTransferStatusId()));
            System.out.println("Amount: $" + transfer.getAmount());
            System.out.println("--------------------------------------------");
        } else {
            System.out.println("Transfer with ID " + transferId + " not found.");
        }
    }*/


}
