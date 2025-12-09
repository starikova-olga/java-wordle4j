package ru.yandex.practicum;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
    private final List<String> userInputs;
    private final PrintStream log;
    private String answer;
    private int steps = 6;
    private String candidate;
    private List<String> dictionary;
    private Set<String> invalidWords = new HashSet<>();
    private Map<Integer, Character> correctLetters = new HashMap<>();


    public WordleGame(List<String> dictionary) {

        this.dictionary = dictionary;
        this.userInputs = new ArrayList<>();
        this.log = new PrintStream(System.out);
    }

    public void startGame() {
        Random random = new Random();

        String randomWord;
        do {
            randomWord = dictionary.get(random.nextInt(dictionary.size()));
        } while (randomWord.length() != 5);
        randomWord = WordleDictionary.normaliseWord(randomWord);
        answer = randomWord;
    }

    public String compareWords(String rawCandidate, String solution) {

        String candidate = WordleDictionary.normaliseWord(rawCandidate);
        if (userInputs.contains(candidate)) {
            throw new IllegalArgumentException("Слово уже было использовано.");
        }
        StringBuilder feedback = new StringBuilder();

        for (int i = 0; i < candidate.length(); i++) {
            char candidateChar = candidate.charAt(i);
            char solutionChar = solution.charAt(i);
            if (candidateChar == solutionChar) {
                feedback.append("+");
            } else if (solution.indexOf(candidateChar) != -1) {
                feedback.append("^");
            } else {
                feedback.append("-");
            }
        }
        System.out.println(feedback);
        return feedback.toString();
    }

    // Метод для предложения слова- подсказки
    public List<String> suggestWord() {
        List<String> suggestions = new ArrayList<>();
        for (String word : dictionary)
            if (!userInputs.contains(word) && word.length() == 5) {
                suggestions.add(word);
            }
        return suggestions;
    }

    // получение подсказки на основе предыдущих вводов
    public String getHint(String mask, List<String> userInputs, String candidate) {

        List<String> possibleWords = suggestWord();
        List<String> filteredWords = new ArrayList<>();

        for (String word : possibleWords) {
            if (!invalidWords.contains(word) && isMatch(word, mask, candidate)) {
                filteredWords.add(word);
            }

        }
        if (filteredWords.isEmpty()) {
            return "Подсказка недоступна";
        } else {
            Random random = new Random();
            int index = random.nextInt(filteredWords.size());
            String hint = filteredWords.get(index);


            if (!isMatch(hint, mask, candidate)) {
                invalidWords.add(hint);
            }
            return hint;
        }
    }

    public boolean isMatch(String word, String mask, String candidate) {
        if (word == null || mask == null || candidate == null) {
            return false;
        }

        if (candidate.isEmpty()) {
            return true;
        }

        for (int i = 0; i < 5; i++) {
            char w = word.toLowerCase().charAt(i);
            char m = mask.charAt(i);
            char c = candidate.toLowerCase().charAt(i);

            switch (m) {
                case '+':
                    if (w != c) return false;
                    else break;
                case '^':
                    if (!candidate.toLowerCase().contains(w + "") || c == w) return false;
                    else break;
                case '-':
                    if (candidate.toLowerCase().contains(w + "")) return false;
                    else break;
                default:
                    throw new RuntimeException("unexpected mask symbol: " + m);
            }
        }
        return true;
    }

    public boolean isValidWord(String word) {
        if (word.length() != 5) {
            return false;
        }

        for (char c : word.toCharArray()) {
            if (Character.isDigit(c) || Character.isWhitespace(c)) {
                return false;
            }
        }

        if (!dictionary.contains(word)) {
            return false;
        }

        return true;
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public void decreaseAttempts() {
        steps--;
    }
}

