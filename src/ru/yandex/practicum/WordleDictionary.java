package ru.yandex.practicum;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    List<String> dictionary;

    public WordleDictionary(List<String> dictionary) {
        this.dictionary = dictionary;
    }

    public String getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(dictionary.size());
        return dictionary.get(index);
    }
    public static String normaliseWord(String word) {
        return word.toLowerCase().replace("ё", "е").trim();
    }
// Метод для добавления слова
    public void addWord(String word) throws EmptyWordException {
        if (word == null || word.isEmpty()) {
            throw new EmptyWordException("Введённое слово не должно быть пустым.");
        }
        if (word.length() == 5) {
           word = normaliseWord(word);
            dictionary.add(word);
        } else {
            System.out.println("Слово должно содержать ровно 5 символов.");
        }
    }
    // Метод проверки наличия слова в словаре
    public boolean isWorldInDictionary(String word) throws WordNotFoundException {
        word = normaliseWord(word);
        if (!dictionary.contains(word)) {
            throw new WordNotFoundException("Слово не найдено в словаре: " + word);
        }
    return true;
    }

    // метод для побуквенной проверки слов и подсчёта количества совпадений
    public static int compareWordsByLetters(String word1, String word2) {
        int matches = 0;
        for (int i = 0; i <Math.min(word1.length(), word2.length()); i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                matches++;
            }
        }
        return matches;
    }

}

