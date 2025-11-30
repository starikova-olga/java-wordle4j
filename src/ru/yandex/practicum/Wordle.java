package ru.yandex.practicum;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public static final String WORDS_FILE = "words_ru.txt";
private final PrintWriter log;

public class Wordle {


    Wordle(PrintWriter log) {
        this log = log;
    }

    public static void main(String[] args) {
        PrintWriter logFile;
        try {
            logFile = new PrintWriter("log.txt");
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла логов: " + e.getMessage());
        }
    }

}
