package cs1302.p3;

import cs1302.gen.UrgencyQueue;
import cs1302.gen.Node;
import java.util.function.Predicate;


/**
 * LinkedUrgencyQueue.
 *
 * <p>
 * {@inheritDoc}
 */
public class LinkedUrgencyQueue<Type extends Comparable<Type>>
    extends BaseLinkedUrgencyQueue<Type> {

    /**
     * Construct a {@code LinkedUrgencyQueue}.
     */
    public LinkedUrgencyQueue() {
        super();
    } // LinkedUrgencyQueue

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
        // for first instance of enqueue and when head is less urgent
        if (this.size == 0 || this.head.getItem().compareTo(item) < 0) {
            Node<Type> newNode = new Node<>(item);
            newNode.setNext(this.head);
            this.head = newNode;
            this.size++;
            return true;
        } // if
        // for any item that is less urgent
        if (this.head.getItem().compareTo(item) > 0) {
            Node<Type> newNode = new Node<>(item);
            Node<Type> order = this.head;
            Node<Type> order2 = this.head;
            if (this.head.hasNext()) {
                order2 = this.head.getNext();
            } // if
            while (order.getItem().compareTo(item) > 0 && order.hasNext()
                   && order2.getItem().compareTo(item) > 0) {
                order = order.getNext();
                if (order2.hasNext()) {
                    order2 = order2.getNext();
                } // if
            } // while
            while (order2.getItem().compareTo(item) == 0 && order2.hasNext()) {
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
        // when head is equal, checks if anything behind the head is also equal
        if (this.head.getItem().compareTo(item) == 0) {
            Node<Type> newNode = new Node<>(item);
            Node<Type> order = this.head;
            Node<Type> order2 = this.head;
            if (this.head.hasNext()) {
                order2 = this.head.getNext();
            } // if
            if (this.head.getItem().compareTo(item) == 0 && order2.hasNext()) {
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

        // creates a new urgency queue that will store the items that are removed
        // from the original queue. The Node order is used to move through the queue.
        UrgencyQueue<Type> uq = new LinkedUrgencyQueue<>();
        Node<Type> order = this.head;

        // loops through num amount of times dequeing the original queue and copying
        // those items into the new queue that will be returned.
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

        // uq is the new urgency queue that is created while order is used to read
        // through the list checking each item if it returns true based on the predicate.
        UrgencyQueue<Type> uq = new LinkedUrgencyQueue<>();
        Node<Type> order = this.head;

        // while loop will only execute for all the items in the list except the last one.
        // This is because hasNext() won't run for the final item because it's next will be null.
        while (order.hasNext()) {
            if (cond.test(order.getItem())) {
                uq.enqueue(order.getItem());
            } // if
            order = order.getNext();
        } // while

        // this if-statement is for the final item in the original list.
        if (cond.test(order.getItem())) {
            uq.enqueue(order.getItem());
        } // if
        return uq;
    } // filter

} // LinkedUrgencyQueue<Type extends Comparable<Type>>
