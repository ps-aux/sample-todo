package sk.luce.data.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sk.luce.data.Todo;
import sk.luce.data.UserAccount;

@SuppressWarnings("unused") //callback methods
@Component
@RepositoryEventHandler
public class TodoRepoEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(TodoRepoEventHandler.class);

    @Autowired
    private AccountRepo accountRepo;

    /**
     * Sets the user account of the new todo item before saving
     */
    @HandleBeforeCreate
    public void beforeCreate(Todo todo) {
        //TODO inject somehow Authentication, UserDetails, Principal or the context. No static methods!.
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount acc = accountRepo.findByName(name);

        todo.setUserAccount(acc);

        logger.debug("Creating {}", todo);
    }

    @HandleBeforeSave
    public void beforeSave(Todo todo) {
        logger.debug("Modifying {}", todo);
    }

    @HandleBeforeDelete
    public void beforeDelete(Todo todo) {
        logger.debug("Deleting {}", todo);
    }
}
