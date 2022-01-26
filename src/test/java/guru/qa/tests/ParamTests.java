package guru.qa.tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.time.Duration;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@DisplayName("Класс с параметризированными тестами для ДЗ номер 8")
public class ParamTests {

    @ValueSource(strings = {"Поставка алкогольной продукции для реализации в бизнес-зале (вокзал Нижний Новгород)",
            "Поставка елей и предметов интерьера"})
    @DisplayName("Поиск тендеров ешоп")
    @ParameterizedTest(name = "{0}")
    public void eshop(String searchValue){

        Configuration.browserSize = "1590x850";
        open("https://eshoprzd.ru/home");
        $(byText("Закупки")).parent().click();
        $(byText("Закупки ОАО «РЖД» до 500 тыс.руб.")).click();
        $("#filterInput").setValue(searchValue);
        $(".glyphicon-search").click();
        $(".ui-select-highlight.h_green").parent().$(byText(searchValue)).should(appear);

        clearBrowserCookies();
        closeWebDriver();
    }

    @CsvSource(value = {
            "qa-guru-1; qaguru1",
            "qa-guru-2; qaguru2"
    }, delimiter = ';'
    )
    @DisplayName("Авторизация в яндекс почте")
    @ParameterizedTest(name = "Логин {0} и пароль {1}")
    public void yandexAuth(String login, String pass){

        Configuration.browserSize = "1590x850";
        open("https://mail.yandex.ru/");
        $(byText("Войти")).parent().click();

        $("#passp-field-login").setValue(login).pressEnter();
        $("#passp-field-passwd").setValue(pass).pressEnter();
        $(".user-account_left-name").$(byText(login)).should(appear);

        clearBrowserCookies();
        closeWebDriver();
    }

    static Stream<Arguments> dataForYandexMessage(){
        File file = new File("src/test/resources/txt.txt");
        return Stream.of(
                Arguments.of("qa-guru-1", "qaguru1", "qa-guru-2", file , "1590x850"),
                Arguments.of("qa-guru-2", "qaguru2", "qa-guru-1", file, "900x600")
        );
    }

    @MethodSource("dataForYandexMessage")
    @DisplayName("Отправка почты в Яндексе")
    @ParameterizedTest(name = "Отправка адресату {2}")
    public void yandexMessage(String login, String pass, String receiver, File file, String browserSize){

        Configuration.browserSize = browserSize;
        open("https://mail.yandex.ru/");

        $(byText("Войти")).parent().click();
        $("#passp-field-login").setValue(login).pressEnter();
        $("#passp-field-passwd").setValue(pass).pressEnter();

        $(".mail-ComposeButton").should(appear, Duration.ofMillis(10000)).click();
        $(".MultipleAddressesDesktop-Field [spellcheck='false']").setValue(receiver + "@yandex.ru");
        $(".cke_htmlplaceholder[placeholder='Напишите что-нибудь']").setValue("Привет!");
        $("input[type='file']").uploadFile(file);
        $(byText("Отправить")).parent().parent().parent().click();

        $(byText("Письмо отправлено")).should(appear);

        clearBrowserCookies();
        closeWebDriver();
    }

}
