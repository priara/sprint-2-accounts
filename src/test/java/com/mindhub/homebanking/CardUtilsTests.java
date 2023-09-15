package com.mindhub.homebanking;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static com.mindhub.homebanking.utils.CardUtils.generateCvv;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.mindhub.homebanking.utils.CardUtils.generateCreditNumber;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CardUtilsTests {

    @Test
    public void cardNumberIsCreated(){

        String cardNumber = generateCreditNumber();

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }

    @Test
    public void testCardNumberFormat() {
        String cardNumber = generateCreditNumber();
        assertTrue(cardNumber.matches("\\d{4}\\d{4}\\d{4}\\d{4}"));
    }


    @Test
    public void cardCVVIsCreated(){

        int cardCVV = generateCvv();

        assertThat(cardCVV, is(notNullValue()));

    }


    @Test
    public void testCVVLength() {
        int cardCVV = generateCvv();
        assertThat(cardCVV, allOf(greaterThanOrEqualTo(100), lessThanOrEqualTo(999)));
    }




}
