package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

        private int transferId;
        private int transferTypeId; // 1 for Request, 2 for Send
        private int transferStatusId; // 1 for Pending, 2 for Approved, 0 for Rejected
        private int fromUserId;
        private int toUserId;
        private BigDecimal amount;

        // Constructor
        public Transfer() {
            this.transferId = transferId;
            this.transferTypeId = transferTypeId;
            this.transferStatusId = transferStatusId;
            this.fromUserId = fromUserId;
            this.toUserId = toUserId;
            this.amount = amount;
        }
    public Transfer(int transferId, int transferTypeId, int transferStatusId, int fromUserId, int toUserId, BigDecimal amount) {
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

        public int getTransferTypeId() {
            return transferTypeId;
        }

        public void setTransferTypeId(int transferTypeId) {
            this.transferTypeId = transferTypeId;
        }

        public int getTransferStatusId() {
            return transferStatusId;
        }

        public void setTransferStatusId(int transferStatusId) {
            this.transferStatusId = transferStatusId;
        }

        public int getFromUserId() {
            return fromUserId;
        }

        public void setFromUserId(int fromUserId) {
            this.fromUserId = fromUserId;
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


    }

