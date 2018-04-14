# Questions

## How would your solution perform if the rate of events increased dramatically?

If the rate of events increased, the performance would depend on two factors.
Firstly, the speed at which the data can stored would affect the performance.
Since my solution uses in-memory storage the storage speed should be very quick.

Secondly, since my solution relies on in memory storage, if the program runs out of memory
it would crash. In order to remedy this issue I would switch to database storage which would solve
the memory issue. However this would have the knock-on effect of slowing the program down as it would
now be interacting with an external storage mechanism possibly hosted on another machine.

## How can you test it?
In order to test my solution I would generate a larger data file to send through the program to
simulate increased rate.

If this was an HTTP-based application I would consider using a tool like gatling to send varying
amounts of requests per second to the program. Gatling would be a useful tool as it can test
the response time allowing you to see how quickly the data is being processed.

## What would be your potential bottlenecks?
Since my program uses in-memory storage the primary bottleneck is how much memory is available.
If I was to switch to a database then the bottleneck would shift to the database and how well it can
read and write data.
