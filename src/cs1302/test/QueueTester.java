package cs1302.test;

import cs1302.oracle.OracleLinkedUrgencyQueue;
import cs1302.oracle.OracleCustomLinkedUrgencyQueue;
import cs1302.gen.UrgencyQueue;
import cs1302.gen.Node;
import cs1302.p3.BaseLinkedUrgencyQueue;
import cs1302.p3.LinkedUrgencyQueue;
import cs1302.p3.CustomLinkedUrgencyQueue;
import java.util.function.Consumer;
import cs1302.oracle.model.Person;
import java.util.List;
import java.util.Comparator;
import cs1302.oracle.model.Movie;
import java.time.Year;
import java.util.function.IntFunction;
import java.util.function.Predicate;

/**
 * QueueTester.
 *
 * <p>
 * {@inheritDoc}
 */
public class QueueTester {


    // QueueTester is used to test the 3 queues with various methods created below
    // that are called in the main method.
    public static void main(String[] args) {
        //System.out.println();
        //QueueTester.urgencyQueue1();
        //System.out.println();
        //QueueTester.urgencyQueue2();
        /**System.out.println();
        QueueTester.urgencyQueue3();
        System.out.println();
        /**
        QueueTester.urgencyQueue4();
        System.out.println();
        QueueTester.urgencyQueue5();*/

        //QueueTester.testDequeue();
        //QueueTester.testSize();
        //QueueTester.testEnqueue();
        //QueueTester.testToArray(); // toarray for linked
        QueueTester.testCustom(); // has an toarray for custom
        //QueueTester.testFilter();
    } // main

    /**
     * testFilter().
     */
    public static void testFilter() {
        UrgencyQueue<String> queue = new LinkedUrgencyQueue<>();
        queue.enqueue("hello");
        queue.enqueue("Help");
        queue.enqueue("A");
        System.out.println(queue);

        Predicate<String> fil = new Predicate<>() {
                @Override
                public boolean test(String m) {
                    return m.contains("e");
                } // test
            };
        System.out.println(queue.filter(fil));

        System.out.println(queue);

    } // testFilter

    /**
     * testCustom().
     */
    public static void testCustom() {
        // get some movie objects -- first 10 from IMDb top movies list
        List<Movie> movies = Movie.Data.IMDB_TOP_MOVIES.subList(0, 10);

        // define "printMovie" action
        Consumer<Movie> printMovie = (Movie movie) -> {
            Year year = movie.year(); // java.time.Year object
            String name = movie.name();
            System.out.printf("%s: %s\n", year, name);
        };

        // use the "printMovie" action to print each movie
        for (Movie movie : movies) {
            printMovie.accept(movie);
        } // for

        System.out.println();

        // define movie comparator that orders by year
        Comparator<Movie> byYear = (Movie a, Movie b) -> {
            // Movie does not have a compareTo method, but java.time.Year does
            return a.year().compareTo(b.year());
        };

        // create an urgency queue
        UrgencyQueue<Movie> queue = new CustomLinkedUrgencyQueue<Movie>(byYear);

        // enqueue the items -- level of urgency based on cmp.compare(Movie, Movie)
        queue.enqueueAll(movies);

        Predicate<Movie> fil = new Predicate<>() {
                @Override
                public boolean test(Movie m) {
                    return m.year().getValue() > 2000;
                } // test
            };

        //System.out.println(queue.filter(fil));
        IntFunction<Movie[]> gen = new IntFunction<>() {
                @Override
                public Movie[] apply(int i) {
                    return new Movie[i];
                } // apply
            };

        // toArray
        System.out.println(queue.toArray(gen));
        System.out.println(queue.toArray(gen).toString());
        Movie[] arr = queue.toArray(gen);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        } //

        // dequeue all items -- use "printMovie" action from earlier
        //queue.dequeueMany(queue.size(), printMovie);

        //System.out.println(queue.size()); // 0
    } // testCustom

    /**
     * testToArray().
     */
    public static void testToArray() {
        IntFunction<String[]> gen = new IntFunction<>() {
                @Override
                public String[] apply(int i) {
                    return new String[i];
                } // apply
            };
        IntFunction<String[]> gen2 = null;

        UrgencyQueue<String> queue = new LinkedUrgencyQueue<>();
        queue.enqueue("B");
        queue.enqueue("Z");
        queue.enqueue("A");

        System.out.println(queue);
        String[] arr = queue.toArray(gen);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        } //
        System.out.println(queue.toArray(gen).toString());

        //System.out.println(queue.toArray(gen2)); // npe

    } // testToArray

    /**
     * testDequeue().
     */
    public static void testDequeue() {
        UrgencyQueue<Integer> queue = new LinkedUrgencyQueue<>();
        //queue.dequeue(); // ise
        queue.enqueue(10);
        queue.enqueue(3);
        queue.enqueue(5);
        //queue.dequeue(null); // npe
        Consumer<Integer> printNum = (Integer num) -> {
            System.out.printf("dequeued %d\n", num);
        };
        queue.dequeue(printNum);
    } //testDequeue

    /**
     * testEnqueue().
     */
    public static void testEnqueue() {
        UrgencyQueue<String> queue = new LinkedUrgencyQueue<>();
        List<String> names = Person.Data.names();
        queue.enqueueAll(names);
        System.out.println(queue);
    } // testEnqueue

    /**
     * testSize().
     */
    public static void testSize() {
        UrgencyQueue<Integer> queue = new LinkedUrgencyQueue<>();
        System.out.println(queue.size()); // 0
        System.out.println(queue); // []
        System.out.println(queue.enqueue(3)); // true
        System.out.println(queue.enqueue(0)); // true
        System.out.println(queue); // [[3], [0]]
        System.out.println(queue.size()); // 2
        System.out.println(queue.enqueue(19));
        queue.enqueue(4);
        System.out.println(queue);
        System.out.println(queue.dequeueMany(2));
        System.out.println(queue);
        //System.out.println(queue.dequeue()); // 3
        //System.out.println(queue.size()); // 1
        //System.out.println(queue.peek()); // 0
        //queue.clear();
        //System.out.println(queue.size()); // 0
        //System.out.println(queue.peek()); // ise
        //System.out.println(queue.dequeue()); // ise
        //System.out.println(queue.enqueue(null)); // npe
    } // testSize()

    /**
     * Example 1: Comparable Upper Bound.
     */
    public static void urgencyQueue1() {
        // new queue
        UrgencyQueue<Integer> queue = new LinkedUrgencyQueue<Integer>();

        // enqueue some items
        queue.enqueue(2);
        queue.enqueue(1);
        queue.enqueue(3);
        queue.enqueue(0);

        // define a "printNum" action
        Consumer<Integer> printNum = (Integer num) -> {
            System.out.printf("dequeued %d\n", num);
        };

        // dequeue and print all items
        queue.dequeueMany(1, null);
    } // urgencyQueue1

    /**
     * Example 2: Another Comparable Upper Bound.
     */
    public static void urgencyQueue2() {
        // new queue
        UrgencyQueue<Person> queue = new LinkedUrgencyQueue<Person>();

        // define some items -- store items in a java.util.List<Person> object
        Iterable<Person> people = List.<Person>of(
            new Person(23, "Bob"),
            new Person(23, "Sue"),
            new Person(24, "Ash"),
            new Person(22, "Cal")
            );

        // enqueue all items
        queue.enqueueAll(people);

        // define a "printPerson" action using a lambda expression
        Consumer<Person> printPerson = (Person person) -> {
            System.out.println(person);
        };

        // dequeue and print all items
        queue.dequeueMany(queue.size(), printPerson);
    } // urgencyQueue2

    /**
     * Example 3: Comparator Constructor Parameter.
     */
    public static void urgencyQueue3() {
        // use a provided comparator
        Comparator<Person> byAge = Person.Order.byAge();

        // create queue
        UrgencyQueue<Person> queue = new CustomLinkedUrgencyQueue<Person>(byAge);

        // define some people
        Iterable<Person> people = List.<Person>of(
            new Person(23, "Bob"),
            new Person(23, "Sue"),
            new Person(24, "Ash"),
            new Person(22, "Cal")
            );

        // enqueue some people
        queue.enqueueAll(people);

        // define a "printPerson" action
        Consumer<Person> printPerson = (Person person) -> {
            System.out.println(person.age() + ": " + person.name());
        };

        // dequeue and print all items
        //queue.dequeueMany(queue.size(), printPerson);
        System.out.println(queue.dequeueMany(2));
        System.out.println();
        System.out.println(queue);
        queue.clear();
        System.out.println(queue);
        System.out.println(queue.size());
    } // urgencyQueue3

    /**
     * Example 4: Reversing the Urgency Order.
     */
    public static void urgencyQueue4() {
        // use a provided comparator
        Comparator<Person> byAgeReversed = Person.Order.byAge().reversed();

        // create queue
        UrgencyQueue<Person> queue = new CustomLinkedUrgencyQueue<Person>(byAgeReversed);

        // define some people
        Iterable<Person> people = List.<Person>of(
            new Person(23, "Bob"),
            new Person(23, "Sue"),
            new Person(24, "Ash"),
            new Person(22, "Cal")
            );

        // enqueue some people
        queue.enqueueAll(people);

        // define a "printPerson" action
        Consumer<Person> printPerson = (Person person) -> {
            System.out.println(person.age() + ": " + person.name());
        };

        // dequeue and print all items
        queue.dequeueMany(queue.size(), printPerson);
    } // urgencyQueue4

    /**
     * Example 5: Another Comparator Constructor Parameter.
     */
    public static void urgencyQueue5() {
        // get some movie objects -- first 10 from IMDb top movies list
        List<Movie> movies = Movie.Data.IMDB_TOP_MOVIES.subList(0, 10);

        // define "printMovie" action
        Consumer<Movie> printMovie = (Movie movie) -> {
            Year year = movie.year(); // java.time.Year object
            String name = movie.name();
            System.out.printf("%s: %s\n", year, name);
        };

        // use the "printMovie" action to print each movie
        for (Movie movie : movies) {
            printMovie.accept(movie);
        } // for

        System.out.println();

        // define movie comparator that orders by year
        Comparator<Movie> byYear = (Movie a, Movie b) -> {
            // Movie does not have a compareTo method, but java.time.Year does
            return a.year().compareTo(b.year());
        };

        // create an urgency queue
        UrgencyQueue<Movie> queue = new CustomLinkedUrgencyQueue<Movie>(byYear);

        // enqueue the items -- level of urgency based on cmp.compare(Movie, Movie)
        queue.enqueueAll(movies);

        // dequeue all items -- use "printMovie" action from earlier
        queue.dequeueMany(queue.size(), printMovie);
    } // urgencyQueue5

} // QueueTester
