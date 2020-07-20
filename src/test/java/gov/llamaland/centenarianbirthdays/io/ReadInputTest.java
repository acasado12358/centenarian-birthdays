package gov.llamaland.centenarianbirthdays.io;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class ReadInputTest {

    @Test
    public void testReadCitizens() throws IOException {
        String citizenFile = "data/citizensTest.csv";
        assertNotNull(ReadInput.readCitizens(citizenFile));
        assertEquals(ReadInput.readCitizens(citizenFile).size(), 3);
    }

    @Test(expected = IOException.class)
    public void testReadCitizensFileNotExist() throws IOException {
        String citizenFile = "data/citizensback.csv";
        ReadInput.readCitizens(Paths.get(citizenFile));
    }

    @Test
    public void testReadOptOutEmail() throws IOException {
        String optOutEmailFile = "data/optoutTest.csv";
        assertNotNull(ReadInput.readOptOutEmail(optOutEmailFile));
        assertEquals(ReadInput.readOptOutEmail(optOutEmailFile).size(), 2);
    }

    @Test(expected = IOException.class)
    public void testReadOptOutEmailFileNotExist() throws IOException {
        String optOutEmailFile = "data/optoutback.csv";
        assertNotNull(ReadInput.readOptOutEmail(optOutEmailFile));
    }

    @Test
    public void testForValidEmail() {

        String email1 = "user@domain.com";
        String email2 = "user.name@domain.com";
        String email3 = "user#@domain.co.in";

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        assertEquals((Boolean) pattern.matcher(email1).matches(), Boolean.TRUE);
        assertEquals((Boolean) pattern.matcher(email2).matches(), Boolean.TRUE);
        assertEquals((Boolean) pattern.matcher(email3).matches(), Boolean.TRUE);

    }

    @Test
    public void testForInvalidEmail() {

        String email1 = "user#domain.com";
        String email2 = "@yahoo.com";

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        assertEquals((Boolean) pattern.matcher(email1).matches(), Boolean.FALSE);
        assertEquals((Boolean) pattern.matcher(email2).matches(), Boolean.FALSE);
    }
}
