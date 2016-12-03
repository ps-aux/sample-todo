package sk.luce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sk.luce.data.Todo;
import sk.luce.data.UserAccount;
import sk.luce.data.repo.AccountRepo;
import sk.luce.data.repo.TodoRepo;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class DbFiller {

    private static final Logger logger = LoggerFactory.getLogger(DbFiller.class);

    @Autowired
    private TodoRepo todoRepo;

    @Autowired
    private AccountRepo accountRepo;


    @PostConstruct
    @Transactional
    public void fillData() {

        logger.info("Filling db with data");

        fillUser("alice",
                new String[]{
                        "see doctor",
                        "uninstall Windows",
                        "take shower"});

        fillUser("bob",
                new String[]{
                        "cook dinner",
                        "bake a cake",
                        "buy apples",
                        "call the doctor",
                        "clean kitchen",
                        "go to gym"});

        logger.info("Db filled");
    }


    private void fillUser(String name, String... todos) {

        UserAccount user = new UserAccount();
        user.setName(name);
        user.setPassword(name + "..");

        UserAccount savedUser = accountRepo.save(user);

        Arrays.stream(todos)
                .map(t -> {
                    Todo obj = new Todo();
                    obj.setText(t);
                    obj.setUserAccount(savedUser);
                    return obj;
                }).forEach(todoRepo::save);

    }

}
