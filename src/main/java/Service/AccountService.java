package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account createAccount(Account account) {
        // Validate username
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank.");
        }
        // Validate password length
        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            throw new IllegalArgumentException("Password must be longer than 4 characters.");
        }
        // Delegate to DAO
        return accountDAO.saveAccount(account);
    }

    // public List<Account> getAllAccounts() {
    //     return accountDAO.getAllAccounts();
    // }

    // public Account getAccountById(int id) {
    //     Account account = accountDAO.getAccountById(id);
    //     if (account == null) {
    //         throw new IllegalArgumentException("Account with ID " + id + " not found.");
    //     }
    //     return account;
    // }

    // public boolean deleteAccountById(int id) {
    //     if (!accountDAO.deleteAccountById(id)) {
    //         throw new IllegalArgumentException("Failed to delete account with ID " + id);
    //     }
    //     return true;
    // }
}