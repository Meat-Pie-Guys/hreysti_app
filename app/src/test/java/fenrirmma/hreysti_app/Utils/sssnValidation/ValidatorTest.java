package fenrirmma.hreysti_app.Utils.sssnValidation;

import org.junit.Test;

import fenrirmma.hreysti_app.Utils.ssnValidation.Validator;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Notandi on 14.12.2017.
 */

public class ValidatorTest {
    @Test
    public void isValidSSNTest() throws Exception{
        assertEquals(true, Validator.isValidSSN("1104872159"));
    }

    @Test
    public void isNotValidSSNTest() throws Exception{
        assertEquals(false, Validator.isValidSSN("0123456789"));
    }
}

