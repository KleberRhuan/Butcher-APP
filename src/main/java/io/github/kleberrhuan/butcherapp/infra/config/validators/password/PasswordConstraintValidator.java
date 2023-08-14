package io.github.kleberrhuan.butcherapp.infra.config.validators.password;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import org.passay.*;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {


    @Override
    public void initialize(final ValidPassword arg0) {

    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        Properties props = new Properties();
        InputStream inputStream = getClass()
                .getClassLoader().getResourceAsStream("passay.properties");
        props.load(inputStream);
        MessageResolver resolver = new PropertiesMessageResolver(props);
        PasswordValidator validator = new PasswordValidator(resolver, List.of(
//                Password Length need to be between 8 and 30
                new LengthRule(8, 30),
//                At least one Upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
//                At least one Lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
//                At least one digit
                new CharacterRule(EnglishCharacterData.Digit, 1),
//                At least one special character
                new CharacterRule(EnglishCharacterData.Special, 1),
//                No whitespace
                new WhitespaceRule(),
//                No repeating characters
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
//              No repeating numbers
                 new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)
            ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(",", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
