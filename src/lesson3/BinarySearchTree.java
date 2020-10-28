package lesson3;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     *
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    /*
    Неодходимые для рассмотрения случаи были взяты из книги
    "Алгоритмы: построение и анализ" (Т.Кормен, Ч.Лейзерсона Р.Ривест, К.Штайн)

     Рассматриваются 3 случая:
            1) У узла нет потомков
            2) У узла есть только один потомок
            3) У узла есть оба потомка
     */
    //H - Высота дерева
    //Трудоемкость = O(Н) - худший случай
    //             = O(logH) - лучший случай
    //Ресурсоемкость = O(1)
    @Override
    public boolean remove(Object o) {
        if (root == null || !contains(o)) return false;
        //Если в корень пустой или элемента нет в дерево
        T t = (T) o;
        root = removeNode(root, t);
        size--;
        //Уменьшаем размер дерева
        return true;
    }

    /*
    1) Если потомков нет,то удаляемый узел просто удаляется
    2) Если есть правый или левый потомок, то узел заменяется этим потомком
    3) Если удаляемый элемент является корнем поддерва и имеет два потомка,
    то его необходимо заменить на минимальный элемент из правого поддерева,
    и соответственно, удалитьэтот правый элемент из дерева
     */
    private Node<T> removeNode(Node<T> tempRoot, T value) {

        int comparison = value.compareTo(tempRoot.value);

        if (comparison > 0) {

            //Идем к правому потомку
            assert tempRoot.right != null;
            tempRoot.right = removeNode(tempRoot.right, value);

        } else if (comparison < 0) {

            //Идем к левому потомку
            tempRoot.left = removeNode(tempRoot.left, value);

        } else {

            //Случай, когда у узла нет потомков
            if (tempRoot.right == null && tempRoot.left == null) return null;

            //Случаи, когда у узла либо левый, либо правый потомок
            else if (tempRoot.left == null) return tempRoot.right;
            else if (tempRoot.right == null) return tempRoot.left;

            //Случай, когда у узла есть оба потомка
            else {

                //Поиск минимума в правом поддереве
                Node<T> minNode = new Node<>(minimum(tempRoot.right).value);
                minNode.left = tempRoot.left;
                minNode.right = tempRoot.right;
                tempRoot = minNode;
                tempRoot.right = removeNode(tempRoot.right, tempRoot.value);
            }
        }
        return tempRoot;
    }

    /*
    Вспомогательный метод, возращающий минимальный элемент
    поддерева с корнем в заданном узде node
    Взят из книги
        "Алгоритмы: построение и анализ" (Т.Кормен, Ч.Лейзерсона Р.Ривест, К.Штайн)
     */
    private Node<T> minimum(Node<T> node){
        if (node == null) throw new NoSuchElementException();
        Node<T> temp = node;
        while (temp.left != null){
            temp = temp.left;
        }
        return temp;
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {

        private Stack<Node<T>> stack = new Stack<>();
        private Node<T> currNode = null;

        private void pushToLeft(Node<T> node){
            if (node != null){
                stack.push(node);
                pushToLeft(node.left);
            }
        }

        private BinarySearchTreeIterator() {
            pushToLeft(root);

        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         *
         * Средняя
         */

        //Трудоемкость = O(1)
        //Ресурсоемкость = O(1)

        @Override
        public boolean hasNext() {

            return !stack.isEmpty();
        }

        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         *
         * Средняя
         */

        //N - Количество элементов
        //Трудоемкость = O(N)
        //Ресурсоемкость = O(1)

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();

            Node<T> node = stack.pop();
            currNode = node;
            pushToLeft(node.right);

            return node.value;
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         *
         * Сложная
         */

        //H - Высота дерева
        //Трудоемкость = O(Н) - худший случай
        //             = O(logH) - лучший случай
        //Ресурсоемкость = O(1)
        @Override
        public void remove() {
            if (currNode == null) throw new IllegalStateException();
            BinarySearchTree.this.remove(currNode.value);
            currNode = null;
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}