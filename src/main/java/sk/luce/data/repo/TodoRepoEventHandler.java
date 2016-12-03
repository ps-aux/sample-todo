package sk.luce.data.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sk.luce.data.Todo;
import sk.luce.data.UserAccount;

@Component
@RepositoryEventHandler
public class TodoRepoEventHandler {

    @Autowired
    private AccountRepo accountRepo;

    /**
     * Sets the user account of the new todo item before saving
     */
    @HandleBeforeCreate
    public void setAccount(Todo todo) {
        //TODO inject somehow Authentication, UserDetails, Principal or the context. No static methods!.
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAccount acc = accountRepo.findByName(name);

        todo.setUserAccount(acc);
    }
}
