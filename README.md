<head>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet" />
</head>

# Java 21 Virtual Threads and Structured Concurrency

## Java Threads and Scalability

* Task types
* Concurrency and Parallelism
* Non Blocking IO
* Introduce Project Loom

### Task Types

```mermaid

%%{ init: { 'flowchart': { 'curve': 'cardinal' } } }%%
flowchart LR
    subgraph External World
        browser(("Browser"))
    end
    subgraph Typical Web Application
        application["\nWeb Application\n(Process)\n"]
        database_1[(Database 1)]
        database_2[(Database 2)]
        ms_1{{"Microservice 1\n(Process)"}}
        ms_2{{"Microservice 2\n(Process)"}}

    end
    application --> external_api
    browser -->|User Request| application
    browser -->|User Request| application
    browser -->|User Request| application
    application <--> database_1
    application <--> database_2
    application <--> ms_1
    application <--> ms_2
```

### Process Per Request (CGI)

```mermaid

flowchart LR
    web-server["Web Server\n(Process)"]
    handler-1["User 1 Handler\nCGI Script\n(Process)"]
    handler-2["User 2 Handler\nCGI Script\n(Process)"]
    handler-3["User 3 Handler\nCGI Script\n(Process)"]
    external-call["\n\n\n\n\n\n       "]
    external-call -->|User 1 Request| web-server
    external-call -->|User 2 Request| web-server
    external-call -->|User 3 Request| web-server
    web-server <-->|" (1) Start Process "| handler-1
    web-server <-->|" (2) Start Process "| handler-2
    web-server <-->|" (3) Start Process "| handler-3
```

* Process is heavyweight
* Limited number of processes per machine
  * Scalability issues
  * Cannot support large number of users
* Expensive Process startup and termination time
* Difficult to share data or communicate between Processes
* FastCGI
  * Pooling of Processes
  * CGI processes are started upfront for performance

### Thread Per Request

```mermaid
flowchart LR
    user-request["\n\n\n\n\n"]
    subgraph web-application["fa:fa-internet-explorer Web Application"]
        thread-1-start(("start"))
        thread-1-finish(("end"))
        thread-2-start(("start"))
        thread-2-finish(("end"))
        thread-3-start(("start"))
        thread-3-finish(("end"))
    end
    user-request -->|User 1 Request| web-application
    user-request -->|User 2 Request| web-application
    user-request -->|User 3 Request| web-application
    thread-1-start -->|" 1. Thread process "| thread-1-finish
    thread-2-start -->|" 2. Thread process "| thread-2-finish
    thread-3-start -->|" 3. Thread process "| thread-3-finish
```

* Threads are lightweight
  * But has its own stack
* Can handle larger number of concurrent users
* Can share data or communicate between threads
* Improved Performance
  * No extra process to deal with
* Easy to understand
* Easy to debug

### Concurrency and Parallelism

#### Concurrency

* Multiple independent tasks are making progress but may not execute at the *same* time
* Appearance of simultaneous execution (parallelism)
* Concurrency is about dealing with lots of things at once
* CPU time slicing

#### Parallelism

* Multiple dependent sub-tasks are executing at the *same* time
* Multiple cores needed
* No parallelism in single core

#### Synchronous Call

* Sequential execution of code
* Easy to understand 
* Easy to debug

#### Asynchronous Call

* Does not wait for call to complete
* Callbacks, Futures...
* More complex to understand 
* In java, user Threads

### Java Platform Threads

* Java Threads are mapped to OS Threads
* Java Threads are heavyweight
* Java Threads are expensive to create
* Java Threads are expensive to context switch
* Java Threads are expensive to terminate
* Java Threads are expensive to synchronize and communicate
* Java Threads are expensive to manage
