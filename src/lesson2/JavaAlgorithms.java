package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.util.Arrays;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     * <p>
     * Общий комментарий: решение из Википедии для этой задачи принимается,
     * но приветствуется попытка решить её самостоятельно.
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    //Пусть n - кол-во букв в самой длинной строке
    //Трудоемкость = O(n^2)
    //Ресурсоемкость = O(n^2) - худший случай (кол-во букв в обеих строках = n)
    //               = O(n) - лучший случай (кол-во букв в одной из строк = 1)
    static public String longestCommonSubstring(String first, String second) {

        if (first == null || second == null || first.length() == 0 || second.length() == 0) return "";
        if (first.equals(second)) return first;

        //matrix for data about matches
        int[][] matrix = new int[first.length()][second.length()];

        int maxLength = 0; //for the longest match
        int maxI = 0; // for the end index of the longest match

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    if (i != 0 && j != 0) { //if matrix[i][j] IS NOT the beginning of the diagonal
                        matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    } else { //if matrix[i][j] IS the beginning of the diagonal
                        matrix[i][j] = 1;
                    }
                    if (matrix[i][j] > maxLength) { //length check
                        maxLength = matrix[i][j];
                        maxI = i;
                    }
                }
            }
        }//return the substring of first
        return first.substring(maxI - maxLength + 1, maxI + 1);
    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    //Решено при помощи алгоритма Решето Эратосфена
    //Пусть n = limit
    //Трудоемкость = O(sqrt(n) * log(log(n)))
    //Ресурсоемкость = O(n) - худший случай (кол-во букв в обеих строках = n)
    static public int calcPrimesNumber(int limit) {
        if (limit <= 1) return 0;

        boolean[] array = new boolean[limit + 1];
        Arrays.fill(array, true);

        int i = 2;
        //it is sufficient to check numbers <= sqrt(limit), that's why O(sqrt(n))
        while (i * i <= limit) {
            if (array[i]) {
                //first time, when i = 2, all even numbers will be removed;
                //than all numbers divisible by 3, and so on...that's why O(log(log(n)))
                for (int j = i * i; j <= limit; j += i) {
                    array[j] = false;
                }
            }
            i++;
        }
        int result = 0; //amount of prime numbers
        for (int k = 2; k < limit + 1; k++) {
            if (array[k]) result++; //count prime numbers
        }
        return result;
    }
}