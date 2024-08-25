package cs1302.p3;

import cs1302.gen.UrgencyQueue;
import cs1302.gen.Node;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.Iterator;

/**
 * BaseLinkedUrgencyQueue.
 *
 * <p>
 * {@inheritDoc}
 */
public abstract class BaseLinkedUrgencyQueue<Type> implements UrgencyQueue<Type> {

    int size;
    Node<Type> head;

    /**
     * Construct a {@code BaseLinkedUrgencyQueue}. This constructor is never
     * intended to be called directly via {@code new}; instead, it should only
     * be called in child class constructors using {@code super()}.
     */
    public BaseLinkedUrgencyQueue() {
        this.size = 0;
        this.head = null;
    } // BaseLinkedUrgencyQueue

    /**
     * Method #2: size().
     * Returns the number of items in this urgency queue.
     *
     * @return the number of items in this urgency queue.
     */
    @Override
    public int size() {
        return this.size;
    } // size

    /**
     * Method #3: peek().
     * Retrieves, but does not remove, the most urgent item in this urgency queue.
     *
     * @return the most urgent item in the queue.
     * @throws IllegalStateException - if there are no items in the queue.
     */
    @Override
    public Type peek() {
        if (this.size == 0) {
            throw new IllegalStateException("no items in the queue.");
        } // if
        return this.head.getItem();
    } // peek

    /**
     * Method #4: toString().
     * Returns a string representation of this urgency queue.
     * The string representation consists of each item in the queue,
     * from most urgent to least urgent, in the descending urgency order,
     * separated by the characters ", " (comma and space)
     * with opening and closing square ([, ]) at the beginning and end, respectively.
     * Each item of the queue is represented by the string value returned by its toString() method.
     *
     * @return the string representation of this urgency queue
     */
    @Override
    public String toString() {
        String str = "[";
        Node<Type> order = this.head;

        // this while loop executes the first and second-to-last items in
        // the queue while outside the loop the last item is added to the String.
        if (order != null) {
            while (order.hasNext()) {
                str += order.getItem().toString() + ", ";
                order = order.getNext();
            } // while
        } // if
        // protects from the program crashing when an empty urgency queue is printed.
        // empty queue will look like []
        if (this.head != null) {
            str += order.getItem().toString();
        } // if
        str += "]";
        return str;
    } // toString

    /**
     * Method #5: dequeue().
     * Retrieves and removes the most urgent item in this urgency queue.
     *
     * @return the most urgent item in the queue.
     * @throws IllegalStateException - if there are no items in the queue.
     */
    @Override
    public Type dequeue() {
        if (this.size == 0) {
            throw new IllegalStateException("there are no items in the queue.");
        } // if

        // urgent stores the most urgent item in the list which is returned.
        // head then shifts to the next Node<> and size is decreased by 1.
        Type urgent = this.head.getItem();
        this.head = this.head.getNext();
        this.size--;
        return urgent;
    } // dequeue

    /**
     * Method #6: dequeue(action).
     * Removes the most urgent item in this urgency queue
     * and performs the given action on the removed item.
     *
     * @param action - the action to be performed on the removed item.
     * @throws NullPointerException - if the specified action is null.
     * @throws IllegalStateException - if there are no items in the queue.
     */
    @Override
    public void dequeue(Consumer<Type> action) {
        if (action == null) {
            throw new NullPointerException("specified action is null.");
        } // if
        if (this.size == 0) {
            throw new IllegalStateException("there are no items in the queue.");
        } // if

        // urgent is created to bookmark the most urgent item. Then, the method
        // accept(Type t) within every Consumer<Type> reference is called
        //with urgent as the parameter. Then the current head is shifted to the
        // one after exactly as the non-parameter dequeue() method works.
        Type urgent = this.head.getItem();
        action.accept(urgent);
        this.head = this.head.getNext();
        this.size--;
    } // dequeue

    /**
     * Method #7: dequeueMany(num, action).
     * Removes the most urgent num-many items in this urgency queue
     * and performs the given action (using action.accept) on each item.
     *
     * @param num - the number of items to remove.
     * @param action - the action to be performed on the removed items.
     * @throws NullPointerException - if the specified action is null.
     * @throws IllegalArgumentException - if num < 0.
     * @throws IllegalStateException - if num > size().
     */
    @Override
    public void dequeueMany(int num, Consumer<Type> action) {
        if (action == null) {
            throw new NullPointerException("specified action is null.");
        } // if
        if (num < 0) {
            throw new IllegalArgumentException("num < 0");
        } // if
        if (num > this.size()) {
            throw new IllegalStateException("num > size().");
        } // if

        // similar to the previous method of dequeue(action).
        // only difference is the usage of a for-loop to execute the command
        // num amount of times.
        Type urgent;
        for (int i = 0; i < num; i++) {
            urgent = this.head.getItem();
            if (this.head.hasNext()) {
                action.accept(urgent);
                this.head = this.head.getNext();
                this.size--;
            } else {
                action.accept(urgent);
                this.size--;
            } // if-else
        } // for
    } // dequeueMany

    /**
     * Method #8: clear().
     * Removes all items from this urgency queue.
     */
    @Override
    public void clear() {
        this.size = 0;
        this.head = null;
    } // clear

    /**
     * Method #9: enqueueAll().
     * Enqueues the items contained in the specified Iterable into this urgency queue.
     * This method should return false if the specified Iterable is empty.
     * Otherwise, this method either returns true or throws an exception upon failure.
     *
     * @param <SubType> - the type of the items to be added.
     * @param items - the items to add to this queue.
     * @return true if this queue is changed as a result of the call; false otherwise.
     * @throws NullPointerException - if items is null.
     * @throws IllegalArgumentException - if any of the items in the specified Iterable are null.
     */
    @Override
    public <SubType extends Type> boolean enqueueAll(Iterable<SubType> items) {
        if (items == null) {
            throw new NullPointerException("items is null.");
        } // if

        // items.iterator() returns an Iterator object iter and nullChecker
        // with the values of items. int count is used to check if items is
        // empty.
        Iterator<SubType> nullChecker = items.iterator();
        int count = 0;
        Iterator<SubType> iter = items.iterator();

        // this for-each loop will go through each item and throw an exception
        // if any are null. Also increments count.
        for (SubType nul : items) {
            if (nullChecker.next() == null) {
                throw new IllegalArgumentException("items in the specified Iterable are null.");
            } // if
            count++;
        } // for each

        // if count which may or may not have changed based on the first for-each loop
        // returns a value of 0, then the method ends returning false to show
        // that nothing in the list was changed.
        if (count == 0) {
            return false;
        } // if

        // enqueues each item into the current list.
        for (SubType val : items) {
            this.enqueue(iter.next());
        } // for-each
        return true;
    } // enqueueAll

    /**
     * Method #10 toArray(generator).
     * Returns an array containing all of the objects in this UrgencyQueue in proper sequence
     * (from first to last element, by urgency).
     * The generator parameter accepts a reference to any method that takes an integer,
     * which is the size of the desired array, and produces an array of the desired size.
     *
     * @param generator - a function which produces a new array of the desired type
     * and the provided size.
     * @return an array containing all of the items in this queue in proper sequence.
     * @throws NullPointerException - if generator is null.
     */
    @Override
    public Type[] toArray(IntFunction<Type[]> generator) {
        if (generator == null) {
            throw new NullPointerException("generator is null.");
        } // if

        // IMPORTANT: When testing toArray, i noticed that if you toString()
        // the results it will return the memory location. But if you run a
        // for loop that prints each individual index of the array then
        // you will see that all the items have been converted into the array.

        // created the array with the same size as the list and a Node
        // that acts as a reader of the list.
        Type[] arr = generator.apply(this.size);
        Node<Type> order = this.head;

        // loops through the array arr copying the item at the given position
        // till the array is full and all items are copied.
        for (int i = 0; i < arr.length; i++) {
            arr[i] = order.getItem();
            if (order.hasNext()) {
                order = order.getNext();
            } // if
        } // for
        return arr;
    } // toArray

} // BaseLinkedUrgencyQueue<Type>
