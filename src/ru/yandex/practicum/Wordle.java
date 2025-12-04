package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */

public class Wordle {
    public static final String WORDS_FILE = "words_ru.txt";
    private final PrintWriter log;

    public Wordle(PrintWriter log) {
        this.log = log;
    }


    public static void main(String[] args) {

        try (PrintWriter log = new PrintWriter(new FileWriter("log.txt"))) {
            Wordle wordle = new Wordle(log);
            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.loadDictionary(WORDS_FILE);
            WordleGame game = new WordleGame(dictionary);

            String mask = "+++++";
            wordle.playGame(game, dictionary, mask);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WordNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void playGame(WordleGame game, WordleDictionary dictionary, String mask) throws WordNotFoundException {
        game.startGame();
        log.println("Добро пожаловать в игру");
        Scanner scanner = new Scanner(System.in);

        List<String> userInputs = new ArrayList<>();
        System.out.println("Угадайте слово из 5 букв, у вас есть 6 попыток \nEnter - ввод слова или подсказка");

        while (game.getSteps() > 0) {

            System.out.println("Ждём ввода слова (осталось попыток): " + game.getSteps() + ")");

            String candidate = scanner.nextLine();


            if (candidate.isEmpty()) {
                log.println("Пользователь воспользовался подсказкой.");
                String hint = game.getHint(mask, userInputs);

                log.println("Подсказка: " + hint);
                System.out.println("Подсказка: " + hint);
                continue;
            } else if (candidate.length() == 5) {
                userInputs.add(candidate);
            } else {
                System.out.println("Слово должно состоять из 5 букв.");
                continue;
            }

            try {
                if (!dictionary.isWordInDictionary(candidate)) {
                    System.out.println("Слово не найдено в словаре. Попробуйте другое слово.");
                    log.println("Неверное слово: " + candidate);
                    continue;
                }
            } catch (WordNotFoundException e) {
                System.out.println(e.getMessage());
            }

            log.println("Пользователь ввёл слово: " + candidate);
            String feedBack = game.compareWords(candidate, game.getAnswer());
            log.println("Обратная связь: " + feedBack);

            if (feedBack.equals("+++++")) {
                log.println("Поздравляем,вы выиграли!");
                break;
            }
            game.decreaseAttempts();
        }

        if (game.getSteps() == 0) {
            log.println("Вы проиграли. Загаданное слово было: " + game.getAnswer());

            System.out.println("Вы проиграли. Загаданное слово: " + game.getAnswer());

        }
    }
}
