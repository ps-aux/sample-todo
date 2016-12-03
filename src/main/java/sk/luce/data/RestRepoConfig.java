package sk.luce.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import sk.luce.data.validation.TodoValidator;

@Configuration
public class RestRepoConfig extends RepositoryRestConfigurerAdapter {

/*    @Override
    protected void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener v) {

    }*/

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener listener) {
        super.configureValidatingRepositoryEventListener(listener);

        TodoValidator validator = new TodoValidator();
        listener.addValidator("beforeSave", validator);
        listener.addValidator("beforeCreate", validator);
    }
}
