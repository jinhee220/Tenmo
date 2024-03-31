package com.techelevator.tenmo.model;

import java.math.BigDecimal;


/*
Changed all Long's to ints
 */
public class Transfer {

    private int transferId;
    private int fromUserId;
    private int toUserId;
    private BigDecimal amount;
    private int transferStatusId;
    private int transferTypeId;
    private TransferStatus status;
    private int fromAccountId;
    private int toAccountId;

    public Transfer() {
        // Default constructor
    }

    public Transfer(int transferId, int fromUserId, int toUserId, BigDecimal amount, TransferStatus status) {
        this.transferId = transferId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.status = status;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int fromUserId, int toUserId, BigDecimal amount,int fromAccountId, int toAccountId) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
    }

    // Getters and setters
    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }
//    public void setTransferTypeId(int transferTypeIdId) {
//        this.transferTypeId = transferTypeId;
//    }

    public void setTransferTypeId(int transferTypeId ) {
        this.transferTypeId = transferTypeId;
    }
    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", amount=" + amount +
                ", status=" + status +
                '}';
    }
}


