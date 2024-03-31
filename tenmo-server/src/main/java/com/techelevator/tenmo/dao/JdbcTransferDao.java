package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// Should implement TransferDao

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void createTransfer(Transfer newTransfer) {
        String sql = " INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                " VALUES (?, ?, ?, ?, ?)   RETURNING transfer_id ; ";
        try {
            int newTransferId =   jdbcTemplate.queryForObject( sql ,
                    int.class,
                    newTransfer.getTransferTypeId(),
                    newTransfer.getTransferStatusId(),
                    newTransfer.getFromUserId(),
                    newTransfer.getToUserId(),
                    newTransfer.getAmount() );

           //  newTransfer.setTransferId(transferId); // Set the generated transfer_id to the newTransfer object

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public List<Transfer> getTransfersList(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from IN ( SELECT account_id FROM account WHERE user_id = ?) OR account_to IN ( SELECT account_id FROM account WHERE user_id = ?) " +
                "ORDER BY transfer_id desc;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        try {
        while (results.next()) {
            Transfer transfer =mapRowToTransfer(results);
            transfers.add(transfer);
        }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfers;
    }

    public List<Transfer> getPendingTransfersList(int userId) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE (account_from = ? OR account_to = ?) AND transfer_status_id = 1";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            pendingTransfers.add(mapRowToTransfer(results));
        }
        return pendingTransfers;
    }


    public Transfer getTransferById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        }
        return null;
    }


    //we dont need this one
    public Transfer updateTransferStatus(int transferId, int newStatusId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        try {
            int rowsUpdated = jdbcTemplate.update(sql, newStatusId, transferId);
            if (rowsUpdated == 1) {
                return getTransferById(transferId); // Return the updated transfer
            } else {
                throw new DaoException("Failed to update transfer status");
            }
        } catch (DataAccessException ex) {
            throw new DaoException("Error updating transfer status", ex);
        }
    }


    @Override
    public void updateTransfer(Transfer updateTransfer) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id=? " +
                "WHERE transfer_id = ?;";
        try {
            int status = updateTransfer.getTransferStatusId();
            int transferId = updateTransfer.getTransferId();
            jdbcTemplate.update(sql, status ,transferId );

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setFromUserId(rs.getInt("account_from"));
        transfer.setToUserId(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }


}

