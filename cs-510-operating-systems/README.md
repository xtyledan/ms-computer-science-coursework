<h1 align="center">Week 1 — Process Inspection with Python</h1>


## Learning Objectives

By the end of this module, the following objectives are demonstrated:

- Describe the components of a process  
- Describe how processes are created and destroyed  
- Explain how processes communicate and share data  

## Module Overview

Modern operating systems manage multiple running programs through **process management**. A process represents an instance of a program currently executing. Understanding how processes are created, scheduled, and terminated helps developers design applications that efficiently use system resources.

This module introduces the structure and lifecycle of processes and how operating systems coordinate CPU and memory usage across many active applications. These concepts are foundational for understanding system performance, concurrency, and modern computer architecture.

## Process Inspection Program

The Python program developed for this module uses the **psutil** library to retrieve and analyze information about active system processes.

### How the Code Retrieves Processes

The program retrieves running processes using the `psutil.process_iter()` function. This function creates an iterator that loops through every active process on the system.

The list `['pid', 'name', 'memory_percent', 'cpu_percent']` is passed into the function to request specific process attributes. Limiting the requested data improves efficiency by collecting only the necessary information.

During execution:

- Each process is accessed as a **Process object**
- Process information is stored in `proc.info`
- `proc.info` contains a dictionary of the requested attributes

The program extracts the following values and prints them in a formatted table:

- Process ID
- Process name
- Memory usage percentage
- CPU usage percentage

Column headers are used only to format the output and organize the displayed data.
