package sk.luce.data.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sk.luce.data.Todo;

public class TodoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Todo.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Todo todo = (Todo) target;

        // TODO create messages properties if needed later
        if (todo.getText() == null ||
                todo.getText().isEmpty())
            errors.rejectValue("text", null, "Text cannot be empty");


    }
}
