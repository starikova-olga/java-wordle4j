package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordleTest {
    private static List<String> dictionary;
    private WordleGame game;


    @BeforeAll

    static void setUp() {
        PrintWriter log = new PrintWriter(System.out);
    }

    // проверка запуска игры и выбора случайного слова
    @Test
    void testStartGame() {
         dictionary = Arrays.asList("марка", "мурка", "муляж", "тучка");
         game = new WordleGame(dictionary);
        game.startGame();
        assertNotNull(game.getAnswer());
        assertEquals(5, game.getAnswer().length());
    }

    // проверка корректности обратной связи
    @Test
    void compareWordsTest() {
        game = new WordleGame(List.of("марка"));
        String result = game.compareWords("марка", "муляж");
        assertEquals("+----", result);
    }

    // проверка предложения слов для подсказки
    @Test
    void testSuggestWord() {
         dictionary = Arrays.asList("марка", "мурка", "муляж", "тучка");
         game = new WordleGame(dictionary);
        List<String> suggestions = game.suggestWord();

        for (String word : suggestions) {
            assertEquals(5, word.length());
        }
    }

    // проверка инициализации игры и загрузки словаря
    @Test
    void testGameInitialization() {
        PrintWriter log = new PrintWriter(new OutputStreamWriter(System.out));
        Wordle wordle = new Wordle(log);
        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        WordleDictionary dictionary = null;
        try {
            dictionary = loader.loadDictionary(Wordle.WORDS_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> words = dictionary.getWords();

        WordleGame game = new WordleGame(words);
        assertNotNull(game);
    }

    // проверка логики сравнения слов
    @Test
    void testCompareWords() {
        PrintWriter log = new PrintWriter(new OutputStreamWriter(System.out));
        Wordle wordle = new Wordle(log);
        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        WordleDictionary dictionary = null;
        try {
            dictionary = loader.loadDictionary(Wordle.WORDS_FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<String> words = dictionary.getWords();
        WordleGame game = new WordleGame(words);

        String candidate = "марка";
        String answer = "муляж";
        String expectedFeedback = "+----";
        String actualFeedback = game.compareWords(candidate, answer);

        assertEquals(expectedFeedback, actualFeedback);
    }

}





