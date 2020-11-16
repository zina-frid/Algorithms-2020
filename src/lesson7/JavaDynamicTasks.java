package lesson7;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    //Алгоритм частично взят с сайтa https://neerc.ifmo.ru/wiki  (Задача о наибольшей общей подпоследовательности)
    //Пусть N - длина в 1ой строки
    //Пусть M - длина в 2ой строки
    //Трудоемкость = O(N*M)
    //Ресурсоемкость = O(N*M)
    // Худший случай, когда длины равны (M = N) -> O(N^2)
    // Лучший случай, когда одна из строк равна 1 -> O(N)
    public static String longestCommonSubSequence(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] matrix = new int[firstLength + 1][secondLength + 1];

        for (int i = 1; i < firstLength; i++) {
            for (int j = 1; j < secondLength; j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    matrix[i + 1][j + 1] = matrix[i][j] + 1;
                } else matrix[i + 1][j + 1] = Math.max(matrix[i + 1][j], matrix[i][j + 1]);
            }
        }

        String result = "";
        int i = firstLength;
        int j = secondLength;
        while (i > 0 && j > 0) {
            if (first.charAt(i - 1) == second.charAt(j - 1)) {
                result = first.charAt(i - 1) + result;
                i--;
                j--;
            } else if (matrix[i][j] == matrix[i - 1][j]) {
                i--;
            } else j--;
        }
        return result;

    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    //Алгоритм взят с сайтa https://neerc.ifmo.ru/wiki  (Задача о наибольшей возрастающей подпоследовательности)
    //Трудоемкость = O(N^2)
    //Ресурсоемкость = O(N)
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {

        int size = list.size();
        if (size <= 1) return list;
        //длина наибольшей возрастающей подпоследовательности, оканчивающейся в элементе,с индексом i
        int[] maxLength = new int[size];
        //Массив для восстановления ответа
        int[] recoveryResponse = new int[size];


        for (int i = 0; i < size; i++) {
            maxLength[i] = 1;
            recoveryResponse[i] = -1;
            for (int j = 0; j < size; j++) {
                if (list.get(j) < list.get(i) && maxLength[j] + 1 > maxLength[i]) {
                    maxLength[i] = maxLength[j] + 1;
                    recoveryResponse[i] = j;
                }
            }
        }
        //Bндекс последнего элемента наиб. возраст. посл-ти
        int pos = 0;
        // длина наиб. возраст. посл-ти
        int length = maxLength[0];
        for (int i = 0; i < size; i++) {
            if (maxLength[i] > length) {
                pos = i;
                length = maxLength[i];
            }
        }
        //Восстановление ответа
        //Если честно, не уверена, что лучше:
        // добавлять каждый элемент в начало result
        // или в конце перевернуть последовательность
        List<Integer> result = new ArrayList<>();
        while (pos != -1) {
            result.add(list.get(pos));
            pos = recoveryResponse[pos];
        }

        Collections.reverse(result);
        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
