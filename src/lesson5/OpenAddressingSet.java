package lesson5;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OpenAddressingSet<T> extends AbstractSet<T> {

    private final int bits;

    private final int capacity;

    private final Object[] storage;

    private int size = 0;

    private int startingIndex(Object element) {
        return element.hashCode() & (0x7FFFFFFF >> (31 - bits));
    }

    public OpenAddressingSet(int bits) {
        if (bits < 2 || bits > 31) {
            throw new IllegalArgumentException();
        }
        this.bits = bits;
        capacity = 1 << bits;
        storage = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    @Override
    public boolean contains(Object o) {
        int index = startingIndex(o);
        Object current = storage[index];
        while (current != null) {
            if (current.equals(o)) {
                return true;
            }
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    @Override
    public boolean add(T t) {
        int startingIndex = startingIndex(t);
        int index = startingIndex;
        Object current = storage[index];
        while (current != null) {
            if (current.equals(t)) {
                return false;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) {
                throw new IllegalStateException("Table is full");
            }
            current = storage[index];
        }
        storage[index] = t;
        size++;
        return true;
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблица, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     *
     * Средняя
     */

    //Трудоемкость = O(N) - худший случай
    //             = O(1) - лучший случай
    //Ресурсоемкость = O(1)

    @Override
    public boolean remove(Object o) {

        /*
        int startingIndex = startingIndex(o);
        int index = startingIndex;
        Object current = storage[index];

        while (current != null) {
            if (current.equals(o))  {
                storage[index] = null;
                size --;
                return true;
            }
            index = (index + 1) % capacity;
            if (index == startingIndex) return false;
            current = storage[index];
        }
        return false;
         */


        int index = startingIndex(o);
        Object current = storage[index];

        while (current != null) {
            if (current.equals(o)) {

                //Следующий индекс
                int nextIndex = (index + 1) % capacity;
                //"Удаление" элемента
                storage[index] = null;

                //Этим циклом сдвигаем следующие элементы
                while (storage[nextIndex] != null) {
                    storage[index] = storage[nextIndex];
                    storage[nextIndex] = null;
                    index = nextIndex;
                    nextIndex = (index + 1) % capacity;
                }

                //Уменьшаем размер
                size--;
                return true;
            }
            //Следующий элемент становитс current
            index = (index + 1) % capacity;
            current = storage[index];
        }
        return false;
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    @NotNull
    @Override
    public Iterator<T> iterator() {
        // TODO
        throw new NotImplementedError();
    }
}
