package Service;
import Model.Account;

import static org.junit.Assert.assertThrows;

import DAO.AccountDAO;
public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account){
        if(account.getUsername().isBlank() || account.getPassword().length() < 4)
            return null;
        return accountDAO.insertAccount(account);
    }

    public Account getAccountById(int accountId){
        return accountDAO.getAccountById(accountId);
    }

    public Account verifyUser(Account account){
        return accountDAO.verifyAccount(account);
    }
}
