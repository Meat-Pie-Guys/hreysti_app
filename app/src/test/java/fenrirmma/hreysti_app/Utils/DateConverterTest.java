package fenrirmma.hreysti_app.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Notandi on 14.12.2017.
 */
public class DateConverterTest {


    @Test
    public void convert() throws Exception {
        assertEquals("07/12/2017", DateConverter.convert("Thu, 07 Dec 2017 10:36:00 GMT"));
    }


    @Test
    public void parser() throws Exception {
        assertEquals("20", DateConverter.parser("2016-06-28T20:25:20.260"));
    }

}