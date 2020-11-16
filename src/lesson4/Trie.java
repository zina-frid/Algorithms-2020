package lesson4;

import java.util.*;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {

        //Теперь stack не хранит все значения одновременно
        private final Stack<Pair> stack = new Stack<>();
        //Добавлены две вспомогательные переменные, поттому что иначе мы делаем одно и тоже
        //Использована Pair, потому что так удобнее удалять в remove
        private Pair string = null;
        private Pair next;

        //Трудоемкость = O(N*M)
        //Ресурсоемкость = O(1)
        private Pair isThereNext(){
            Pair result = null;

            //O(N) - количество элементов стеке
            //Однако, эта оценка рчень неоднозначна, так как этто число меняется
            while(!stack.isEmpty()){
                Pair currentPair = stack.pop();

                //O(M) - количество детей у текущего node в currentPair
                for (Map.Entry<Character, Node> child : currentPair.second.children.entrySet()) {

                    if (child.getKey() != (char) 0)
                        stack.push(new Pair(currentPair.first + child.getKey(),child.getValue()));

                    else result = currentPair;
                }
                if (result != null) break;
            }
            return result;
        }

        private TrieIterator() {
            stack.push(new Pair("", root));
            next = isThereNext();
        }

        //Трудоемкость = O(1)
        //Ресурсоемкость = O(1)
        @Override
        public boolean hasNext() {
            return next != null;
        }

        //Трудоемкость = O()
        //Ресурсоемкость = O()
        @Override
        public String next() {
            if (!hasNext()) throw new IllegalStateException();
            string = next;
            next = isThereNext();
            return string.first;
        }

        //Трудоемкость = O(1)
        //Ресурсоемкость = O(1)
        @Override
        public void remove() {
            if (string == null) throw new IllegalStateException();
            string.second.children.remove((char) 0);
            size--;
            string = null;
        }

        //Класс Pair
        public class Pair {
            private String first;
            private Node second;

            public Pair(String first, Node second){
                this.first = first;
                this.second = second;
            }
        }
    }
}
