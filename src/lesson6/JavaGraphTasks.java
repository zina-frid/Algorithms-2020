package lesson6;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     *
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */

    //N - количество вершин
    //Трудоемкость = O(N * N)
    //Ресурсоемкость = O(N)
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {

        //Set для всех множеств независимых вершин
        Set<Set<Graph.Vertex>> finalSets = new HashSet<>();


        for (Graph.Vertex vertex : graph.getVertices()){
            //Set для хранения независимых вершин в данном рпоходе цикла
            Set<Graph.Vertex> temp = new HashSet<>();
            //Set для тех вершин, которые надо пропустить, так как они являются соседями вершин из temp
            Set<Graph.Vertex> unsuitable = new HashSet<>();
            for (Graph.Vertex vertex2 : graph.getVertices()){
                //Проверка, является ли vertex2 соседом vertex и есть ли он в числе тех вершин, которые надо пропустить
                if (!graph.getNeighbors(vertex).contains(vertex2) && !unsuitable.contains(vertex2)) {
                    unsuitable.addAll(graph.getNeighbors(vertex2));
                    temp.add(vertex2);
                }
            }
            finalSets.add(temp);
        }

        //Ищем самое большое множество независимых вершин
        Set<Graph.Vertex> result = new HashSet<>();
        for (Set<Graph.Vertex> set : finalSets) {
            if (set.size() > result.size()) result = set;
        }

        return result;
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */

    //N - количество всех вершин
    //Трудоемкость = O(N!)
    //Ресурсоемкость = O(N^2)
    //Максимальный размер paths = N - 1 + (N - 1) - 1 + (N - 2) - 1 +...+ (N - (N + 1)) =
    // = N + (N - 1) + (N - 2) +...+ 2 + 1 - 1*(N - 1) = |исп. сумму арифм. прогр.| =
    // = N * (N + 1) / 2 - (N - 1) = (N + N^2 - 2N + 2)/2 = (N^2 - N + 2)/2 -> O((N^2 - N + 2)/2) -> O(N^2)
    public static Path longestSimplePath(Graph graph) {

        Set<Graph.Vertex> vertices = graph.getVertices();
        Stack<Path> paths = new Stack<>();
        Path result = new Path();
        int longest = 0;

        for (Graph.Vertex vertex: vertices) paths.push(new Path(vertex));

        while(!paths.isEmpty()){

            //Ищем самый длинный
            Path path = paths.pop();
            if (path.getLength() > longest){
                result = path;
                longest = result.getLength();
            }

            //Соседи последней вершины path
            Set<Graph.Vertex> neighbours = graph.getNeighbors(path.getVertices().get(path.getLength()));
            for (Graph.Vertex neighbour : neighbours){
                if(!path.contains(neighbour)) paths.push(new Path(path, graph, neighbour));
            }
        }
        return result;
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
