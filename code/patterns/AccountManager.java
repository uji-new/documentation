package app.manager;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.password.PasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import app.dao.AccountDao;
import app.error.AuthenticationError;
import app.manager.generic.BaseManager;
import app.model.AccountModel;

@Service
public class AccountManager extends BaseManager {
    @Autowired private AccountDao accountDao;

public AccountModel newGuest() {
    var account = newAccount();
    account.setTransient(true);
    return account;
}

public AccountModel newAccount() {
    return new AccountModel();
}

    public boolean existsAccount(String mail) {
        return accountDao.existsById(mail);
    }

    public AccountModel getAccount(String mail) {
        return accountDao.findById(mail).orElseThrow(AuthenticationError::new);
    }

    public void saveAccount(AccountModel account) {
        accountDao.save(account);
    }

    public void deleteAccount(String mail) {
        accountDao.deleteById(mail);
    }

    @Bean
    public PasswordEncryptor newEncryptor() {
        return new BasicPasswordEncryptor();
    }
}
