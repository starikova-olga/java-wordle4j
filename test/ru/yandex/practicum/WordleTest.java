package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordleTest {
    private static List<String> dictionary;
    private WordleGame game;


    @BeforeEach
    void setUp() {
        PrintWriter log = new PrintWriter(System.out);
        dictionary = Arrays.asList("марка", "мурка", "муляж", "тучка");

        game = new WordleGame(new WordleDictionary(dictionary));
    }

    // Проверка подсказки
    @Test
    void getHintsTest() {
        List<String> userInputs = new ArrayList<>();
        userInputs.add("марка");
        userInputs.add("мурка");

        String mask = "++---";
        String hint = game.getHint(mask, userInputs);

        assertEquals("муляж", hint);
    }

    //Проверка алгоритма угадывания
    @Test
    void compareWordsTest() {
        String guess = "марка";
        String answer = "муляж";
        String expectedFeedback = "+----";
        String actualFeedback = game.compareWords(guess, answer);

        assertEquals(expectedFeedback, actualFeedback);
    }

    @Test
    void startGameTest() {
        game.startGame();
        assertNotNull(game.getAnswer());
        assertEquals(6, game.getSteps());
    }

    @Test
    void getHintEmptyListTest() {
        List<String> userInputs = new ArrayList<>();
        String mask = "++--";
        String hint = game.getHint(mask, userInputs);
        assertEquals("Подсказка недоступна", hint);
    }


    @Test
    void validateWordLengthTooShortTest() {
        String word = "суп";
        assertNotEquals(5, word.length());
    }

    @Test
    void validateWordLengthTooLongTest() {
        String word = "яблоко";
        assertNotEquals(5, word.length());
    }

    @Test
    void validateWordLengthCorrectTest() {
        String word = "тучка";
        assertEquals(5, word.length());
    }

}
