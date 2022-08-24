package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeAll;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WebParametrizedTests {

    @BeforeAll
    static void configure(){
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;

    }

        @CsvSource(value = {
                "props, Render Props",
                "javascript, JavaScript Environment Requirements"
        }, delimiter = ',')
        @ParameterizedTest(name = "Результаты поиска содержат текст {1}, по запросу {0}")
        void checkingForContent(String testData, String expectedResult){
            open("https://reactjs.org/");
            $("input[type=search]").setValue(testData);
            $("div.algolia-docsearch-suggestion--wrapper").shouldBe(visible).click();
            $("header.css-hgc6lu h1").shouldHave(text(expectedResult));
        }



    static Stream<Arguments> viewButtonsWhenChoosingLanguage(){
        return Stream.of(
                Arguments.of("Русский", List.of("Документация","Введение","Блог","Сообщество")),
                Arguments.of("English", List.of("Docs","Tutorial","Blog","Community"))
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Для языка {0} отображаются кнопки {1}")
    void viewButtonsWhenChoosingLanguage(String lang, List<String> expectedButtons){

        open("https://reactjs.org/");
        $("span.css-1rsw1pf").shouldBe(text("Languages")).click();
        $$("ul li div a").find(text(lang)).click();
        $$("nav a.css-hobwqm")
                .filter(visible)
                .shouldHave(CollectionCondition.texts(expectedButtons));
    }


    @ValueSource(strings = {"Начало работы","Введение в хуки"})
    @ParameterizedTest(name = "Поиск по запросу {0} содержит описание")
    void contentIsNotEmpty(String testData){

        open("https://reactjs.org/");
        $("span.css-1rsw1pf").shouldBe(text("Languages")).click();
        $("#gatsby-focus-wrapper>div>div>div>div>div>div>ul:nth-child(2)>li:nth-child(13) > div.css-1cks8n3 > a").shouldBe(text("Русский")).click();
        $("input[type=search]").setValue(testData);
        $("div.algolia-docsearch-suggestion--wrapper").click();
        $$(".css-15weewl p").shouldBe(CollectionCondition.sizeGreaterThan(10));
    }

}
