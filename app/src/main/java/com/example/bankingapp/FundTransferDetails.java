package com.example.bankingapp;

public class FundTransferDetails {
    private String bank_to,payee,account_no,amount,IFSC,remarks, debited_from_account;

    public FundTransferDetails(String bank_to, String payee, String account_no,
                               String amount, String IFSC, String remarks,
                               String debited_from_account) {
        this.bank_to = bank_to;
        this.payee = payee;
        this.account_no = account_no;
        this.amount = amount;
        this.IFSC = IFSC;
        this.remarks = remarks;
        this.debited_from_account = debited_from_account;
    }

    public String getBank_to() {
        return bank_to;
    }

    public String getPayee() {
        return payee;
    }

    public String getAccount_no() {
        return account_no;
    }

    public String getAmount() {
        return amount;
    }

    public String getIFSC() {
        return IFSC;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDebited_from_account() {
        return debited_from_account;
    }
}
