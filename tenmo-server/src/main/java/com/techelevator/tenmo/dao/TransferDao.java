package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    // Get all transfers (sent or received) for current user
    List<Transfer> getTransfersList(int userId);

    // Get all transfers where status LIKE pending for current user
    List<Transfer> getPendingTransfersList(int userId);

    // Get a specific transfer by its ID
    Transfer getTransferById(int transferId);

    // Update Transfer (specifically status but we can update the whole transfer and only change one thing like in the account method)
    void updateTransfer(Transfer updateTransfer);


    // Request a transfer of money from another user --> changed this to create Transfer, requests/sends are all created transfers
    // the only distinction is the transfer type property which can be set within the logic
    void createTransfer(Transfer newTransfer);

    Transfer updateTransferStatus(int transferId, int newStatusId);
}



