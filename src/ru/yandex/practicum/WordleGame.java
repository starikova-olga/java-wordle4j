package ru.yandex.practicum;

import org.junit.platform.commons.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private String answer;

    private int steps;

    private WordleDictionary dictionary;

  private List<String> userInputs = new ArrayList<>();
private LinkedHashMap<String, String> hints = new LinkedHashMap<>();

public WordleGame(WordleDictionary dictionary, String answer) {
    this.dictionary = dictionary;
    this.answer = answer;
}

public String compareWords(String rawCandidate, String solution) {
    String candidate = WordleDictionary.normaliseWord(rawCandidate);

    if (userInputs.contains(candidate) || hints.containsValue(candidate)) {
        throw new IllegalArgumentException("Слово уже было использовано.");
    }
        StringBuilder feedback = new StringBuilder();
for (int i = 0; i < candidate.length(); i++) {
    char candidateChar = candidate.charAt(i);
    char solutionChar = solution.charAt(i);

    switch (solution.indexOf(candidateChar)) {
        case -1:
        feedback.append("-");
        break;
        case i:
            feedback.append("+");
            break;
        default:
            feedback.append("^");
            break;
    }
} return feedback.toString();
    }


    //Анализ совпадений слов с ответом
    public String analyzeGuess(String guess) {
        try {
            if(!dictionary.isWorldInDictionary(guess)) {
                throw new IllegalArgumentException("Слово не найдено в словаре.");
            }
        } catch (WordNotFoundException e) {
            throw new RuntimeException(e);
        }
        int matches = WordleDictionary.compareWordsByLetters(guess, answer);
        userInputs.add(guess);
        steps++;
        return "Совпадений: " + matches;
    }
    // Метод для предложения слова- подсказки
    public List<String> suggestWord() {
        List<String> suggestions = new ArrayList<>();
       for (String word : dictionary.dictionary) {
           if (!userInputs.contains(word) && word.length() == 5) {
               suggestions.add(word);
           }
    }
       return suggestions;
}
// получение подсказки на основе предыдущих вводов
    public String getHint() {
    String hint = "";
    if (userInputs.isEmpty()) {
        hint =dictionary.dictionary.get((int) (Math.random() * dictionary.size());
    } else {
        List<String>possibleWords = suggestWord();
        for (String word : possibleWords) {
            if (!hints.containsValue(word)) {
                hint = word;
                break;
            }
        }
    }
    hints.put("Input " + steps, hint);
    return hint;
    }
}