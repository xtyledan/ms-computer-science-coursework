<h1 align="center"> Thread Management with Python</h1>

## Learning Objectives

By the end of this module, the following objectives are demonstrated:

- Describe components of a thread and the thread control block  
- Explain multi-threading and how it is implemented  
- Contrast how Windows and Linux systems implement threads  

## Module Overview

Modern applications are expected to be both highly functional and highly responsive. As applications grow more complex, it becomes essential to use available system resources efficiently while ensuring that the operating system can safely manage concurrent tasks.

Multi-threading allows applications to separate long-running tasks from the main execution thread. By doing this, the user interface can remain responsive instead of freezing while background operations are performed. These operations often include retrieving data from disk storage, accessing network resources, or interacting with distributed systems.

In many enterprise environments, applications retrieve information from multiple sources simultaneously. Threads enable these operations to occur concurrently, significantly improving responsiveness and overall performance.

This module explores how threads are created, how they operate within a process, and how operating systems manage them. Understanding these concepts helps developers design, troubleshoot, and maintain applications that rely on concurrent execution.

## Thread Management Program

The Python program developed for this module demonstrates basic multi-threading using Python’s `threading` library.

### How the Code Manages Threads

The program creates two threads using the `threading.Thread()` constructor. Each thread is assigned a specific **target function**, which determines the code that will execute when the thread begins running.

When the `start()` method is called, Python requests that the operating system create and schedule native threads within the same process. This allows both functions to run concurrently rather than sequentially.

After starting the worker threads, the main thread continues executing and calls the `join()` method on each thread. The `join()` method pauses the main thread until the worker thread completes its task. This ensures the program does not terminate before all threads have finished executing.

Because thread scheduling is controlled by the operating system, the order of output messages may change each time the program runs.
