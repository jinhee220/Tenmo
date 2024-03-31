package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UtilityService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final UtilityService utilityService = new UtilityService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private AuthenticatedUser currentUser;


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            transferService.setAuthToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();

            } else if (menuSelection == 2) {
                viewTransferHistory();

            } else if (menuSelection == 3) {
                viewPendingRequests();

            } else if (menuSelection == 4) {
                sendBucks();

            } else if (menuSelection == 5) {
                requestBucks();

            } else if (menuSelection == 0) {
                continue;

            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub

        Account account = transferService.getAccount(currentUser.getUser().getId());
        System.out.println(String.format("Current Balance is : %.2f", account.getBalance()));
    }


    private void viewTransferHistory() {
        List<Transfer> transfers = utilityService.getAllTransfers(currentUser.getUser().getId());
        List<Integer> transferIds = utilityService.getTransferIds(transfers);

        int index = 0;
        int size = transfers.size();

        if (size == 0) {
            System.out.println("No transfers available.");
            return;
        }
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID\t\tFrom/To\t\t\t\t\t Amount");
        System.out.println("-------------------------------------------");
        while (index < size) {
            Transfer transfer = transfers.get(index);

            String fromTo;
            if (transfer.getTransferTypeId() == 2) {
                fromTo =  ("To:   " );
            } else {
                fromTo =   ("From: " );
            }

            String otherParty;
            if (transfer.getTransferTypeId() == 2) {
                otherParty = String.valueOf( transferService.getUserByAccountId(transfer.getToUserId()).getUsername()  );
            } else {
                otherParty = String.valueOf( transferService.getUserByAccountId(transfer.getFromUserId()).getUsername() );
            }

            System.out.println(String.format("%1d%10s%-18s $%-10.2f",  transfer.getTransferId(), fromTo,  otherParty , transfer.getAmount()   ));
            index++;
        }

        System.out.println("---------------------");
        int transferId = consoleService.promptForInt("Please enter Transfer ID to view details: ");

        if (transferIds.contains(transferId) ) {
            Transfer seeTransfer = transferService.getTransferById(transferId);
            //get transfer by transferId
            Transfer pendingTransfer = transferService.getTransferById(transferId);
            System.out.println("--------------------------------------------");
            System.out.println("TRANSFER DETAILS");
            System.out.println("--------------------------------------------");
            System.out.println("Id: " + seeTransfer.getTransferId());
            System.out.println("From: " + transferService.getUserByAccountId(seeTransfer.getFromUserId()).getUsername());
            System.out.println("To: " + transferService.getUserByAccountId(seeTransfer.getToUserId()).getUsername());
            switch (seeTransfer.getTransferTypeId()) {
                case 1:
                    System.out.println("Type: Request");
                    break;
                case 2:
                    System.out.println("Type: Send");
                    break;
            }
            switch (seeTransfer.getTransferStatusId()) {
                case 1:
                    System.out.println("Type: Pending");
                    break;
                case 2:
                    System.out.println("Type: Approved");
                    break;
                case 3:
                    System.out.println("Type: Rejected");
                    break;

            }
            System.out.println("Amount: $" + seeTransfer.getAmount());
        } else {
            System.out.println("Please enter a valid transferId!");
        }

        // Now you can proceed with the transfer details based on the entered transferId
    }


    private void viewPendingRequests() {
        // create variables
        List<Transfer> allTransfers = utilityService.getAllTransfers(currentUser.getUser().getId());
        List<Transfer> pendingTransfers = new ArrayList<>();
        List<Integer> pendingTransfersIds = new ArrayList<>();

        //go through transfers list for all transfers with status pending and add to new list
        for (Transfer x : allTransfers) {
            if (x.getTransferStatusId() == 1) {
                pendingTransfers.add(x);
                pendingTransfersIds.add(x.getTransferId());
            }
        }

        if (pendingTransfers.size() > 0) {
            //print pending transfers in format
            System.out.println("-------------------------------------------");
            System.out.println("Pending Transfers");
            //chatGPT reference for string format
            System.out.println("ID          To                     Amount  ");
            System.out.println("-------------------------------------------");
            for (Transfer y : pendingTransfers) {
                String id = Integer.toString(y.getTransferId());
                String userName = transferService.getUserByAccountId(y.getToUserId()).getUsername();
                String amount = y.getAmount().toString();
                //chatGPT reference for string format
                String formatString = String.format("%1s%13s%24s", id, userName, "$" + amount);
                System.out.println(formatString);
            }
            boolean isUserChoosing = true;

            while (isUserChoosing) {
                //prompt userInput for transferId
                int option = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
                //if transferId == 0, return
                if (option == 0) {
                    return;
                }
                //if transferId == to one on list
                if (pendingTransfersIds.contains(option)) {
                    //get transfer by transferId
                    Transfer pendingTransfer = transferService.getTransferById(option);
                    //run actOnPendingRequest()
                    actOnPendingRequest(pendingTransfer);
                    //cut loop
                    isUserChoosing = false;
                }
                else {
                    System.out.println("Please enter a valid transferId!");
                    return;
                }

            }
        } else {
            System.out.println("There are no pending transfers!");
        }
    }


    private void sendBucks() {
        // TODO Auto-generated method stub
        // getAccount from database here and create variables
        Account userAccount = transferService.getAccount(currentUser.getUser().getId());
        List<Integer> allUsers = utilityService.getAllUsers();
        Account receiverAccount;
        int receiverID;
        BigDecimal newBalance;
        BigDecimal amount;


        List<User> availableUsersList = utilityService.printSendRequestUserList(currentUser.getUser());
        // prompt for userId
        receiverID = consoleService.promptForInt("%nEnter ID of user you are sending to (0 to cancel): ");

        // cancel the method
        if (receiverID == 0) {
            return;
        }

        // find user from list
        //if (currentUser.getUser().getId() != receiverID && allUsers.contains(receiverID)) {
        if (currentUser.getUser().getId() != receiverID && allUsers.contains(receiverID)) {
            //User[] users = transferService.getUsers();
            System.out.println(receiverID);


            // prompt for amount
            amount = consoleService.promptForBigDecimal("Enter amount: ");

            // if amount is allowed (greater than 0 and less than/equal to user balance
            if (userAccount.getBalance().compareTo(amount) > 0) {
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    System.out.println(amount);


                    // subtract from currentUser
                    newBalance = userAccount.getBalance().subtract(amount);
                    try {
                        userAccount.setBalance(newBalance);
                    } catch (NullPointerException n) {
                        throw new NullPointerException(n.getMessage());
                    }
                    // transferService.update(account)
                    transferService.updateAccount(userAccount);
                    // add to receiver
                    receiverAccount = transferService.getAccount(receiverID);
                    newBalance = receiverAccount.getBalance().add(amount);
                    try {
                        receiverAccount.setBalance(newBalance);
                    } catch (NullPointerException n) {
                        throw new NullPointerException(n.getMessage());
                    }
                    // transferService.update(account)
                    transferService.updateAccount(receiverAccount);


                    // create transfer with status approved for user and receiver
                    Transfer transfer = new Transfer();
                    transfer.setTransferTypeId(2);   // Send
                    transfer.setTransferStatusId(2); // Approved
                    transfer.setFromUserId(userAccount.getAccountId());
                    transfer.setToUserId(receiverAccount.getAccountId());
                    transfer.setAmount(amount);

                    if (transferService.createTransfer(transfer)) {
                        System.out.println("Transfer successful! ");
                    } else {
                        System.out.println("Transfer failed.");
                    }


                } else {
                    System.out.println("Please Enter an amount that is Greater than Zero ");
                }
            } else {
                System.out.println("Please Enter an amount that is less than your current available balance");
            }

        } else {
            System.out.println("Transfer not be allowed: Please select another Account ID from the list");
        }


    }


    private void requestBucks() {
        // TODO Auto-generated method stub
        // getAccount from database here and create variables
        Account userAccount = transferService.getAccount(currentUser.getUser().getId());
        Account requestedAccount;
        BigDecimal amount;
        int requestedID;


        List<User> availableUsersList = utilityService.printSendRequestUserList(currentUser.getUser());
        // prompt for userId
        requestedID = consoleService.promptForInt("%nEnter ID of user you are requesting to (0 to cancel): ");

        // cancel the method
        if (requestedID == 0) {
            return;
        }
        List<Integer> allUsers = utilityService.getAllUsers();

        // find user from list
        //if (currentUser.getUser().getId() != requestedID && allUsers.contains(requestedID)) {
        if (currentUser.getUser().getId() != requestedID && allUsers.contains(requestedID)) {
            //User[] users = transferService.getUsers();
            System.out.println(requestedID);

            // prompt for amount
            amount = consoleService.promptForBigDecimal("Enter amount: ");

            // if amount is allowed (greater than 0
            if (amount.compareTo(BigDecimal.ZERO) > 0) {

                // getAccount from database here and create variables
                requestedAccount = transferService.getAccount(requestedID);

                // create transfer with status pending for user and receiver to requested
                Transfer transfer = new Transfer();
                transfer.setTransferTypeId(1);   // Request
                transfer.setTransferStatusId(1); // Pending
                transfer.setFromUserId(userAccount.getAccountId());
                transfer.setToUserId(requestedAccount.getAccountId());
                transfer.setAmount(amount);
                if (transferService.createTransfer(transfer)) {
                    System.out.println("Request successful!");
                } else {
                    System.out.println("Request failed.");
                }

            } else {
                System.out.println("Amount should be Greater than Zero ");
            }

        } else {
            System.out.println("Transfer not be allowed: Please select another Account ID from the list");
        }

    }


    //------------------------------------------//
    //--------------Utility Methods-------------//
    //------------------------------------------//


    private void actOnPendingRequest(Transfer transfer) {
        //grab requester transfer using receiverId
        Transfer requesterTransfer = utilityService.getTransferByAccountId(transfer.getToUserId(), transfer);
        //show options
        boolean isUserChoosing = true;

        while (isUserChoosing) {
            System.out.println("1: Approve " +
                    "\n2: Reject " +
                    "\n0: Don't approve or reject " +
                    "\n--------- ");
            int response = consoleService.promptForInt("Please choose an option: ");

            if ( response == 0 || response == 1 || response == 2 ) {
                switch (response) {

                    //approval process if 1
                    case 1:
                        //  if (transfer.getTransferId() == 1 ) {
                        //grab userId from transfer
                        int userAccountId = transfer.getFromUserId();
                        //grab amount from transfer
                        BigDecimal amount = transfer.getAmount();
                        BigDecimal newBalance;
                        Account userAccount = transferService.getAccount(currentUser.getUser().getId());
                        Account toUserAccount = transferService.getAccount(transferService.getUserByAccountId(transfer.getToUserId()).getId());

                        //amount validity check
                        if (userAccount.getBalance().compareTo(amount) >= 0) {
                            //                        if (amount.compareTo(BigDecimal.ZERO) > 0) {
                            // subtract from currentUser
                            newBalance = userAccount.getBalance().subtract(amount);
                            try {
                                userAccount.setBalance(newBalance);
                            } catch (NullPointerException n) {
                                throw new NullPointerException(n.getMessage());
                            }
                            // transferService.update(account)
                            transferService.updateAccount(userAccount);
                            // add to receiver
                            newBalance = toUserAccount.getBalance().add(amount);
                            try {
                                toUserAccount.setBalance(newBalance);
                            } catch (NullPointerException n) {
                                throw new NullPointerException(n.getMessage());
                            }
                            // transferService.update(account)
                            transferService.updateAccount(toUserAccount);
                            //                        } else {
                            //                            System.out.println("Please Enter an amount that is Greater than Zero ");
                            //                        }

                            //set userTransfer status to approved
                            transfer.setTransferStatusId(2);
                            //update userTransfer
                            transferService.updateTransfer(transfer);
                            Transfer acceptedTransfer = transferService.getTransferById(transfer.getTransferId());

                            //set requesterTransfer status to approved
                            requesterTransfer.setTransferStatusId(2);
                            //update requesterTransfer
                            transferService.updateTransfer(requesterTransfer);

                            System.out.println("Transfer " + acceptedTransfer.getTransferId() + " has been approved.");
                            System.out.println();
                            break;

                        } else {
                            System.out.println("Your current available balance is less than the Requested Amount");
                        }
                        // }
                        //rejection process if 2
                    case 2:
                        //set userTransfer status to rejected
                        transfer.setTransferStatusId(3);
                        //update userTransfer
                        transferService.updateTransfer(transfer);
                        Transfer rejectedTransfer = transferService.getTransferById(transfer.getTransferId());

                        //set requestedTransfer status to rejected
                        requesterTransfer.setTransferStatusId(3);
                        //update requestedTransfer
                        transferService.updateTransfer(requesterTransfer);
                        System.out.println("Transfer " + rejectedTransfer.getTransferId() + " has been rejected.");
                        System.out.println();
                        break;

                    //exit method if response 0
                    case 0:
                        isUserChoosing = false;
                        break;

                    //invalid
                    default:
                        System.out.println("Please enter a valid response!");
                        System.out.println();
                        break;
                }

            }
            return;
        }

    }


    //------------------------------------------//
    //--------------Extra Methods---------------//
    //------------------------------------------//

    /*
    private String getPartyName(int userId) {
        // Logic to retrieve and return the name associated with the user ID
        return "User " + userId; // Placeholder
    }
*/
    /*
    private String getTransferType(int typeId) {
        // Logic to retrieve and return the transfer type description based on the type ID
        return "Send"; // Placeholder
    }
*/
    /*
    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        // list status pending balances

        List<Transfer> pendingTransfers = transferService.getPendingTransfersList(currentUser.getUser().getId());
        if (pendingTransfers.isEmpty()) {
            System.out.println("No pending transfers available.");
        } else {
            System.out.println("Pending Transfers");
            System.out.println("ID\tFrom/To\t\tAmount");
            for (Transfer transfer : pendingTransfers) {
                String fromTo;
                String otherParty;
                if (transfer.getTransferTypeId() == 2) {
                    fromTo = "From: ";
                    otherParty = String.valueOf(transfer.getToUserId());
                } else {
                    fromTo = "To:   ";
                    otherParty = String.valueOf(transfer.getFromUserId());
                }
                System.out.println(transfer.getTransferId() + "\t" + fromTo + otherParty + "\t$" + transfer.getAmount());
            }
        }
    }
    */
    /*
    private String getTransferStatus(int statusId) {
        // Logic to retrieve and return the transfer status description based on the status ID
        return "Approved"; // Placeholder

    */
}
