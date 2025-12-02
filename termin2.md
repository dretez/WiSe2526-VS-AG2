The aim of the task is to implement a distributed algorithm using asynchronous
message exchange. 

The algorithm to be implemented determines the greatest common divisor (GCD) of
several numbers. It is to be executed by several processes that communicate via
the network. A process is started for each input number for which the GCD is to
be determined.

The processes should form a ring in which each process can communicate with its
predecessor and successor. To do this, each process is given a reference to its
predecessor, successor, and one of the input numbers when it starts.

Each process should execute the following algorithm, where M is the number
passed to the respective process:

```
# Message y is received
if y < M then
  M = mod(M - 1, y) + 1;
  send M to all neighbours;
end
```

- After all processes have started, each process must communicate its local
  number M to both neighbors. This starts the execution of the algorithm.
- All communication between the processes should be implemented asynchronously 
  and via messages.
- None of the processes may block.
- Frameworks and middleware may be used for communication in the implementation.
  Examples would be actor frameworks (e.g., Akka) or message brokers (e.g., 
  ActiveMQ). 
- The programming language used can be freely chosen. One option would be
  implementation in Java. The algorithm must be adapted to the programming
  language accordingly.
- Communication should run over the network. The processes should be distributed
  evenly across the computers available at the time of execution.
- Communication should run via a standardized human-readable message format,
  such as JSON or XML. Here, too, external libraries are permitted for
  serialization.
- To start the processes, a separate program should be written that implements a
  method that allows the GCD to be determined for a variable number of numbers. 
  This program must start the corresponding processes, wait for a predefined
  time, and query the result.

This start program should also implement the distribution of the processes to
the currently available computers.

1. Define the message format based on the selected standard. The structure must
   be described and an example provided for each type of message.
2. Implement the algorithm in the programming language of your choice.
3. Create a sequence diagram that shows a possible sequence of the algorithm
   for the numbers 108, 76, 12, 60, and 36. The sequence should be shown from
   the call of the method in the start program to the output of the result.
4. How can it be determined that the algorithm has terminated and the result is
   final? Define all sufficient criteria.
   This step does not need to be implemented; a specified waiting time for the
   start program is sufficient for the practical course.

A report must be submitted in the form of a text document containing all
relevant design decisions (e.g., choice of programming language) and results of
the tasks. The source code must be explainable at the time of submission. The
implementation must be executable on the pool computers via the network.