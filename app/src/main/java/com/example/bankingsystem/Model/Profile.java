package com.example.bankingsystem.Model;

import java.util.ArrayList;
import java.util.Calendar;

public class Profile {

    private String name;
    private String userId;
    private String password;
    private String phoneNum;
    private ArrayList<Account> accounts;
    private ArrayList<Payee> payees;
    private long dbId;

    public Profile (String name, String phoneNum, String userId, String password) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.userId = userId;
        this.password = password;
        accounts = new ArrayList<>();
        payees = new ArrayList<>();
    }

    public Profile (String name, String phoneNum, String userId, String password, long dbId) {
        this(name, phoneNum, userId, password);
        this.dbId = dbId;
    }

    public Profile(String name, String phoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
    }

    /**
     * getters used to access the private fields of the profile
     */
    public String getName() {
        return name;
    }
    public String getPhoneNum(){
        return phoneNum;
    }
    public String getUserId() {
        return userId;
    }
    public String getPassword() {
        return password;
    }
    public ArrayList<Account> getAccounts() { return accounts; }
    public ArrayList<Payee> getPayees() { return payees; }
    public long getDbId() { return dbId; }
    public void setDbId(long dbId) { this.dbId = dbId; }

    public void addAccount(String accountName, double accountBalance) {
        String accNo = "A" + (accounts.size() + 1);
        Account account = new Account(accountName, accNo, accountBalance);
        accounts.add(account);
    }
    public void setAccountsFromDB(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public void addTransferTransaction(Account sendingAcc, Account receivingAcc, double transferAmount) {

        sendingAcc.setAccountBalance(sendingAcc.getAccountBalance() - transferAmount);
        receivingAcc.setAccountBalance(receivingAcc.getAccountBalance() + transferAmount);

        int sendingAccTransferCount = 0;
        int receivingAccTransferCount = 0;
        for (int i = 0; i < sendingAcc.getTransactions().size(); i ++) {
            if (sendingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                sendingAccTransferCount++;
            }
        }
        for (int i = 0; i < receivingAcc.getTransactions().size(); i++) {
            if (receivingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                receivingAccTransferCount++;
            }
        }

        sendingAcc.getTransactions().add(new Transaction("T" + (sendingAcc.getTransactions().size() + 1) + "-T" + (sendingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
        receivingAcc.getTransactions().add(new Transaction("T" + (receivingAcc.getTransactions().size() + 1) + "-T" + (receivingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
    }

    public void addAutoTransferTransaction(Account sendingAcc, Account receivingAcc, double transferAmount) {//한달마다 보내기

        sendingAcc.setAccountBalance(sendingAcc.getAccountBalance() - transferAmount);
        receivingAcc.setAccountBalance(receivingAcc.getAccountBalance() + transferAmount);

        int sendingAccTransferCount = 0;
        int receivingAccTransferCount = 0;
        for (int i = 0; i < sendingAcc.getTransactions().size(); i ++) {
            if (sendingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                sendingAccTransferCount++;
            }
        }
        for (int i = 0; i < receivingAcc.getTransactions().size(); i++) {
            if (receivingAcc.getTransactions().get(i).getTransactionType() == Transaction.TRANSACTION_TYPE.TRANSFER) {
                receivingAccTransferCount++;
            }
        }

        sendingAcc.getTransactions().add(new Transaction("T" + (sendingAcc.getTransactions().size() + 1) + "-T" + (sendingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
        receivingAcc.getTransactions().add(new Transaction("T" + (receivingAcc.getTransactions().size() + 1) + "-T" + (receivingAccTransferCount+1), sendingAcc.toTransactionString(), receivingAcc.toTransactionString(), transferAmount));
    }

    private long getDuration(){//한 달 후의 시간을 반환(timemills)
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);

        // move month ahead
        currentMonth++;
        // check if has not exceeded threshold of december

        if(currentMonth > Calendar.DECEMBER){
            // alright, reset month to jan and forward year by 1 e.g fro 2013 to 2014
            currentMonth = Calendar.JANUARY;
            // Move year ahead as well
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }

        // reset calendar to next month
        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // set the calendar to maximum day (e.g in case of fEB 28th, or leap 29th)
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead



        return (thenTime); // this is what you set as trigger point time i.e one month after

    }

    public void addPayee(String payeeName) {
        String payeeID = "P" + (payees.size() + 1);
        Payee payee = new Payee(payeeID, payeeName);
        payees.add(payee);
    }

    public void setPayeesFromDB(ArrayList<Payee> payees) {
        this.payees = payees;
    }
}
