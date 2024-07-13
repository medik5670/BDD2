package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;


import java.net.HttpCookie;
import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement amountField = $("[data-test-id=amount] .input__control");
    private SelenideElement fromField = $("[data-test-id=from] .input__control");
    private SelenideElement button = $("[data-test-id=action-transfer]");

    private SelenideElement errorMassage = $("[data-test-id = error-notification] .notification__content");

    public DashboardPage toTransMoney(String amount, DataHelper.CardInfo cardInfo) {
        makeTransfer(amount, cardInfo);
        return new DashboardPage();
    }

    public void makeTransfer(String sum, DataHelper.CardInfo cardInfo) {
        amountField.setValue(sum);
        fromField.setValue(cardInfo.getCardNumber());
        button.click();
    }

    public void findErrorMassage(String expectedText) {
        errorMassage.shouldHave(text(expectedText));
    }
}

