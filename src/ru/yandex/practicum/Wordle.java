package ru.yandex.practicum;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
try (PrintWriter log = new PrintWriter(new FileWriter("log.txt"))){
Wordle wordle = new Wordle(log);
WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
WordleDictionary dictionary = loader.LoadDictionary(WORDS_FILE);
    WordleGame game = new WordleGame(dictionary);
    wordle.playGame(game);
} catch (IOException e) {
    e.printStackTrace();
} catch (WordNotFoundException e) {
    throw new RuntimeException(e);
}
    }
    private void playGame(WordleGame game) throws WordNotFoundException {
        game.startGame();
        log.println("Добро пожаловать в игру");
        Scanner scanner = new Scanner(System.in);
        int attemptsLeft = 6;
        System.out.println("Угадайте слово из 5 букв, у вас есть 6 попыток \nEnter - ввод слова или подсказка");

        while (attemptsLeft > 0) {
            System.out.println("Ждём ввода слова (осталось попыток): " + attemptsLeft + ")");
            String guess = scanner.nextLine();

            if (guess.isEmpty()) {
                log.println("Пользователь воспользовался подсказкой.");
                String hint = game.getHint();
                log.println("Подсказка: " + hint);
                continue;
            }
            log.println("Пользователь ввёл слово: " + guess);
            String feedBack = game.compareWords(guess, game.analyzeGuess(guess));

            if (feedBack.equals("+++++")) {
                log.println("Поздравляем,вы выиграли!");
                break;
            }

            attemptsLeft--;
        }
        if (attemptsLeft == 0) {
            log.println("Вы проиграли. Загаданное слово было: " + game.suggestWord());



        }

    }

}
