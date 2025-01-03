package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private static final AccountDAO UserRepository = null;
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }public static boolean isUsernameTaken(String username) {
        return UserRepository.getAccountByUsername(username) != null;
    }

    
    public static Account registerUser(Account account) {
        return UserRepository.createAccount(account);
    }
     // TODO: consider modifying functions to throw exceptions so that the controller can handle them 

    public Account createAccount(Account account) {
        // Validate username
      // Delegate to DAO
        return accountDAO.createAccountAccount(account);
    }

    public Account getAccountByUsername(String username) {
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null) {
            // Hide the password
            account.setPassword("");
        }

        return account;
    }

    public Account login(Account account) {
        Account result = accountDAO.getAccountByUsername(account.getUsername());
        
        if (result == null) {
            // Do we throw an exception or return null?
            throw new IllegalArgumentException("Account with username " + account.getUsername() + " not found.");
        }
            
        // validate password... username already validated in AccountDAO's getAccountByUsername function
        if (!result.getPassword().equals(account.getPassword())) {
            return null;
        }
        // Hide the password
        // result.setPassword("");
        return result;
    }

    public Object getAccountById(int id) {
        return accountDAO.getAccountById(id);

    }
}

    // public boolean deleteAccountById(int id) {
    //     if (!accountDAO.deleteAccountById(id)) {
    //         throw new IllegalArgumentException("Failed to delete account with ID " + id);
    //     }
    //     return true;
    // }

 