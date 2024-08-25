#!/bin/bash -ex
javac -Xlint:rawtypes -Xlint:unchecked -Werror -d bin -cp cs1302-urgency-queue.jar src/cs1302/p3/BaseLinkedUrgencyQueue.java
javac -Xlint:rawtypes -Xlint:unchecked -Werror -cp bin:cs1302-urgency-queue.jar -d bin src/cs1302/p3/LinkedUrgencyQueue.java
javac -Xlint:rawtypes -Xlint:unchecked -Werror -cp bin:cs1302-urgency-queue.jar -d bin src/cs1302/p3/CustomLinkedUrgencyQueue.java
javac -Xlint:rawtypes -Xlint:unchecked -Werror -cp bin:cs1302-urgency-queue.jar -d bin src/cs1302/test/QueueTester.java
java -cp bin:cs1302-urgency-queue.jar cs1302.test.QueueTester
