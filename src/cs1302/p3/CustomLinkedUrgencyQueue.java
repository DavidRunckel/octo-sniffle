package cs1302.p3;

import cs1302.gen.UrgencyQueue;
import cs1302.gen.Node;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * CustomLinkedUrgencyQueue.
 *
 * <p>
 * {@inheritDoc}
 */
public class CustomLinkedUrgencyQueue<Type> extends BaseLinkedUrgencyQueue<Type> {

    private Comparator<Type> comparator;

    /**
     * Construct a {@code CustomLinkedUrgencyQueue}.
     *
     * @param cmp a function that lets you determine the urgency order
     *     between two items
     * @throws NullPointerException if {@code cmp} is {@code null}
     */
    public CustomLinkedUrgencyQueue(Comparator<Type> cmp) {
        super();
        if (cmp == null) {
            throw new NullPointerException("cmp is null");
        } // if
        this.comparator = cmp;
    } // CustomLinkedUrgencyQueue

    /**
     * Method #2: enqueue(item).
     * Inserts the specified item into this urgency queue.
     * The inserted item is placed such that it appears after any existing items
     * with the same or greater level of urgency and before any existing items
     * with a lesser level of urgency.
     *
     * @param item - the item to insert
     * @return true if this urgency queue was modified as a result of this call
     * @throws NullPointerException - if item is null
     */
    @Override
    public boolean enqueue(Type item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        } // if
        // for the first addition to the list and for any item that is more urgent than head.
        if (this.size == 0 || this.comparator.compare(this.head.getItem(), item) < 0) {
            Node<Type> newNode = new Node<>(item);
            newNode.setNext(this.head);
            this.head = newNode;
            this.size++;
            return true;
        } // if
        // for when head is more urgent, order2 works through points in the list
        // of the item being equal to another item already in the list in terms of urgency
        if (this.comparator.compare(this.head.getItem(), item) > 0) {
            Node<Type> newNode = new Node<>(item);
            Node<Type> order = this.head;
            Node<Type> order2 = this.head;
            if (this.head.hasNext()) {
                order2 = this.head.getNext();
            } // if
            while (this.comparator.compare(order.getItem(), item) > 0 && order.hasNext()
                   && this.comparator.compare(order2.getItem(), item) > 0) {
                order = order.getNext();
                if (order2.hasNext()) {
                    order2 = order2.getNext();
                } // if
            } // while
            while (this.comparator.compare(order2.getItem(), item) == 0 && order2.hasNext()) {
                order = order.getNext();
                order2 = order2.getNext();
            } // while
            if (order.hasNext()) {
                newNode.setNext(order.getNext());
            } // if
            order.setNext(newNode);
            this.size++;
            return true;
        } // if
        // this is specifically for when the head is equal to the enqueued item
        if (this.comparator.compare(this.head.getItem(), item) == 0) {
            Node<Type> newNode = new Node<>(item);
            Node<Type> order = this.head;
            Node<Type> order2 = this.head;
            if (this.head.hasNext()) {
                order2 = this.head.getNext();
            } // if
            if (this.comparator.compare(order2.getItem(), item) == 0 && order2.hasNext()) {
                order = order.getNext();
                order2 = order2.getNext();
            } // if
            if (order.hasNext()) {
                newNode.setNext(order.getNext());
            } // if
            order.setNext(newNode);
            this.size++;
            return true;
        } // if
        return true;
    } // enqueue

    /**
     * Method #3: dequeueMany(num).
     * Builds and returns a new urgency queue that contains the most urgent num items
     * dequeued from this UrgencyQueue.
     *
     * @param num - the number of items to remove and return.
     * @return a new urgency queue object containing the most urgent num items
     * dequeued from this queue
     * @throws IllegalArgumentException - if num < 0.
     * @throws IllegalStateException - if num > size().
     */
    @Override
    public UrgencyQueue<Type> dequeueMany(int num) {
        if (num < 0) {
            throw new IllegalArgumentException("num < 0");
        } // if
        if (num > this.size()) {
            throw new IllegalStateException("num > size().");
        } // if

        UrgencyQueue<Type> uq = new CustomLinkedUrgencyQueue<>(this.comparator);
        Node<Type> order = this.head;

        // using the new UrgencyQueue<Type> uq, it enqueues the most urgent items in
        // order from the original queue and removes them from the original through
        // size--;.
        for (int i = 0; i < num; i++) {
            uq.enqueue(order.getItem());
            if (order.hasNext()) {
                order = order.getNext();
                head = head.getNext();
            } // if
            this.size--;
        } // for
        return uq;
    } // dequeueMany

    /**
     * Method #4: filter(cond).
     * Builds and returns a new urgency queue consisting of the items of this urgency queue
     * that pass the test specified by the given predicate.
     *
     * @param cond - the predicate used to test items of this queue.
     * @return a reference to the filtered queue.
     * @throws NullPointerException - if the specified predicate is null.
     */
    @Override
    public UrgencyQueue<Type> filter(Predicate<Type> cond) {
        if (cond == null) {
            throw new NullPointerException("specified predicate is null.");
        } // if

        UrgencyQueue<Type> uq = new CustomLinkedUrgencyQueue<>(this.comparator);
        Node<Type> order = this.head;

        // Enqueues items to the new UQ if they return true from cond.test().
        while (order.hasNext()) {
            if (cond.test(order.getItem())) {
                uq.enqueue(order.getItem());
            } // if
            order = order.getNext();
        } // while

        // Checks the final item in the original queue because the while
        // loop won't check it.
        if (cond.test(order.getItem())) {
            uq.enqueue(order.getItem());
        } // if
        return uq;
    } // filter

} // CustomLinkedUrgencyQueue<Type>
