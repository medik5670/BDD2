package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;


class MoneyTransferTest {
    DashboardPage dashboardPage;
    CardInfo firstCardInfo;
    CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;

    @BeforeEach
    void setup() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(0);
        secondCardBalance = dashboardPage.getCardBalance(1);
    }

    @Test
    void shouldToTransferMoneyBetweenAccount() {
        int amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(0) - amount;
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) + amount;
        var transMoney = dashboardPage.selectCardForTransfer(secondCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(amount), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }

    @Test
    void shouldToTransferMoneyBetweenAccountWithNegativeAmount() {
        int amount = generateValidAmount(secondCardBalance);
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) - amount;
        int expectedBalanceFirstCard = dashboardPage.getCardBalance(0) + amount;
        var transMoney = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(amount), secondCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        assertAll(
                () -> assertEquals(expectedBalanceFirstCard, actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard)
        );
    }
    @Test
    void shouldBeErrorMassage() {
        int amount = generateValidAmount(secondCardBalance);
        var thirdCardInfo = getThirdCardInfo();
        var transMoney = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(amount), thirdCardInfo);
        transMoney.findErrorMassage("Ошибка! Произошла ошибка");
    }
}


