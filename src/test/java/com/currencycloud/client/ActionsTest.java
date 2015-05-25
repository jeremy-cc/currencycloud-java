package com.currencycloud.client;

import co.freeside.betamax.junit.Betamax;
import com.currencycloud.client.model.*;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class ActionsTest extends BetamaxTestSupport {

    @Override protected String getAuthToken() { return "4df5b3e5882a412f148dcd08fa4e5b73"; }

    // todo: serialize collections correctly in method body
    @Test
    @Betamax(tape = "can_create")
    public void testCanCreate() throws Exception {
        Beneficiary beneficiary = client.createBeneficiary(
                "Test User", "GB", "GBP", "Test User", null, null, null, "12345678", "sort_code", "123456",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                Collections.singletonList("regular")
        );

        assertThat(beneficiary.getId(), equalTo("081596c9-02de-483e-9f2a-4cf55dcdf98c"));
        assertThat(beneficiary.getBankAccountHolderName(), equalTo("Test User"));
        assertThat(beneficiary.getPaymentTypes(), hasItem("regular"));
        assertThat(beneficiary.getCreatedAt(), equalTo(parseDateTime("2015-04-25T09:21:00+00:00")));
        assertThat(beneficiary.getUpdatedAt(), equalTo(parseDateTime("2015-04-25T09:21:00+00:00")));
    }

    @Test
    @Betamax(tape = "can_retrieve")
    public void testCanRetrieve() throws Exception {
        Beneficiary beneficiary = client.retrieveBeneficiary("081596c9-02de-483e-9f2a-4cf55dcdf98c");

        assertThat(beneficiary.getId(), equalTo("081596c9-02de-483e-9f2a-4cf55dcdf98c"));
        assertThat(beneficiary.getBankAccountHolderName(), equalTo("Test User"));
        assertThat(beneficiary.getPaymentTypes(), hasItem("regular"));
        assertThat(beneficiary.getCreatedAt(), equalTo(parseDateTime("2015-04-25T09:21:00+00:00")));
        assertThat(beneficiary.getUpdatedAt(), equalTo(parseDateTime("2015-04-25T09:21:00+00:00")));
    }

    @Test
    @Betamax(tape = "can_first")
    public void testCanFirst() throws Exception {
        Beneficiary beneficiary = client.firstBeneficiary(
                "Test User", null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null);

        assertThat(beneficiary.getId(), equalTo("081596c9-02de-483e-9f2a-4cf55dcdf98c"));
        assertThat(beneficiary.getBankAccountHolderName(), equalTo("Test User"));
        assertThat(beneficiary.getPaymentTypes(), hasItem("regular"));
        assertThat(beneficiary.getCreatedAt(), equalTo(parseDateTime("2015-04-25T09:21:00+00:00")));
        assertThat(beneficiary.getUpdatedAt(), equalTo(parseDateTime("2015-04-25T10:58:21+00:00")));
    }

    @Test
    @Betamax(tape = "can_find")
    public void testCanFind() throws Exception {
        Beneficiaries beneficiariesData = client.findBeneficiaries();

        List<Beneficiary> beneficiaries = beneficiariesData.getBeneficiaries();
        assertThat(beneficiaries, not(empty()));
        assertThat(beneficiaries.size(), is(1));

        Pagination pagination = beneficiariesData.getPagination();
        assertThat(pagination.getTotalEntries(), equalTo(1));
        assertThat(pagination.getTotalPages(), equalTo(1));
        assertThat(pagination.getCurrentPage(), equalTo(1));
        assertThat(pagination.getPerPage(), equalTo(25));
        assertThat(pagination.getPreviousPage(), equalTo(-1));
        assertThat(pagination.getNextPage(), equalTo(-1));
        assertThat(pagination.getOrder(), equalTo("created_at"));
        assertThat(pagination.getOrderAscDesc(), equalTo(Pagination.SortOrder.asc));
    }

    @Test
    @Betamax(tape = "can_update")
    public void testCanUpdate() throws Exception {
        Beneficiary beneficiary = client.updateBeneficiary(
                "081596c9-02de-483e-9f2a-4cf55dcdf98c", "Test User 2",
                null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null, null, null
        );

        assertThat(beneficiary.getId(), equalTo("081596c9-02de-483e-9f2a-4cf55dcdf98c"));
        assertThat(beneficiary.getBankAccountHolderName(), equalTo("Test User 2"));
    }

    @Test
    @Betamax(tape = "can_delete")
    public void testCanDelete() throws Exception {
        Beneficiary beneficiary = client.deleteBeneficiary("081596c9-02de-483e-9f2a-4cf55dcdf98c");

        assertThat(beneficiary.getId(), equalTo("081596c9-02de-483e-9f2a-4cf55dcdf98c"));
        assertThat(beneficiary.getBankAccountHolderName(), equalTo("Test User 2"));
        assertThat(beneficiary.getPaymentTypes(), hasItem("regular"));
        assertThat(beneficiary.getCreatedAt(), equalTo(parseDateTime("2015-04-25T09:21:00+00:00")));
        assertThat(beneficiary.getUpdatedAt(), equalTo(parseDateTime("2015-04-25T11:06:27+00:00")));
    }

    @Test
    @Betamax(tape = "can_current")
    public void testCanCurrent() throws Exception {
        Account account = client.currentAccount();

        assertThat(account.getId(), equalTo("8ec3a69b-02d1-4f09-9a6b-6bd54a61b3a8"));
        assertThat(account.getPostalCode(), nullValue());
        assertThat(account.getCreatedAt(), equalTo(parseDateTime("2015-04-24T15:57:55+00:00")));
        assertThat(account.getUpdatedAt(), equalTo(parseDateTime("2015-04-24T15:57:55+00:00")));
    }

    // todo: serialize collections correctly in method body
    @Test
    @Betamax(tape = "can_validate_beneficiaries")
    public void testCanValidateBeneficiaries() throws Exception {
        client.setAuthToken("4df5b3e5882a412f148dcd08fa4e5b73");
        List<String> paymentTypes = Collections.singletonList("regular");
        Beneficiary beneficiary = client.validateBeneficiary(
                "GB", "GBP", null, "12345678", "sort_code", "123456",
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                paymentTypes
        );
        assertThat(beneficiary.getPaymentTypes(), Matchers.equalTo(paymentTypes));
        assertThat(beneficiary.getBankCountry(), equalTo("GB"));
        assertThat(beneficiary.getBankName(), equalTo("HSBC BANK PLC"));
        assertThat(beneficiary.getCurrency(), equalTo("GBP"));
        assertThat(beneficiary.getAccountNumber(), equalTo("12345678"));
        assertThat(beneficiary.getRoutingCodeType1(), equalTo("sort_code"));
        assertThat(beneficiary.getBeneficiaryAddress(), empty());
        assertThat(beneficiary.getRoutingCodeValue1(), equalTo("123456"));
        assertThat(beneficiary.getBankAddress(), equalTo(Arrays.asList("5 Wimbledon Hill Rd", "Wimbledon", "London")));
        assertThat(beneficiary.getBankAccountType(), is(CoreMatchers.nullValue()));
    }

    @Test
    @Betamax(tape = "can_use_currency_to_retrieve_balance")
    public void testCanUseCurrencyToRetrieveBalance() throws Exception {
        Balance balance = client.findBalance("GBP");

        assertThat(balance.getId(), equalTo("5a998e06-3eb7-46d6-ba58-f749864159ce"));
        assertThat(balance.getAmount(), equalTo(new BigDecimal("999866.78")));
        assertThat(balance.getCreatedAt(), equalTo(parseDateTime("2014-12-04T09:50:35+00:00")));
        assertThat(balance.getUpdatedAt(), equalTo(parseDateTime("2015-03-23T14:33:37+00:00")));
    }
}