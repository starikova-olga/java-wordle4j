package ru.yandex.practicum;

import java.io.PrintStream;
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
    private List<String> userInputs;
    private LinkedHashMap<String, String> hints = new LinkedHashMap<>();
    private PrintStream log;

    public WordleGame(WordleDictionary dictionary) {

        this.dictionary = dictionary;
        this.userInputs = new ArrayList<>();
        this.log = new PrintStream(System.out);
    }

    public void startGame() {
        steps = 6;
        String randomWord;
        do {
            randomWord = dictionary.getRandomWord();
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
        System.out.println("Обратная связь: " + feedback.toString());
        return feedback.toString();
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
    public String getHint(String mask, List<String> userInputs) {
        if (hints.containsKey(mask)) {
            return hints.get(mask);
        } else {

            List<String> possibleWords = suggestWord();
            List<String> filteredWords = new ArrayList<>();

            for (String word : possibleWords) {
                if (!userInputs.contains(word) && isValidHint(word, mask)) {
                    filteredWords.add(word);
                }
            }
            if (filteredWords.isEmpty()) {
                return "Подсказка недоступна";
            } else {
                Random random = new Random();
                int index = random.nextInt(filteredWords.size());
                String hint = filteredWords.get(index);

                return hint;
            }
        }
    }

    private boolean isValidHint(String word, String mask) {
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            char maskSymbol = mask.charAt(i);

            switch (maskSymbol) {
                case '+':
                    if (letter != word.charAt(i)) {
                        return false;
                    }
                    break;
                case '-':
                    if (word.indexOf(letter) != -1) {
                        return false;
                    }
                    break;
                case '^':
                    if (!word.substring(0, i).contains(String.valueOf(letter)) &&
                            !word.substring(i + 1).contains(String.valueOf(letter))) {
                        return false;
                    }
                    break;
            }
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

