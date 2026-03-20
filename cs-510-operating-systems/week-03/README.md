<h1 align="center"> CPU Scheduling and Memory Management</h1>

## Learning Objectives

By the end of this module, the following objectives are demonstrated:

- Describe the concept of algorithms used with CPU scheduling  
- Describe the difference between physical and logical memory  
- Contrast how Windows and Linux systems implement CPU scheduling  

## Module Overview

Operating systems allow multiple programs to run simultaneously by carefully managing system resources such as CPU time and memory. Many users interact with several applications at once—editing documents, browsing the web, and running background tasks—without noticing the complex scheduling and resource management happening behind the scenes.

To make this possible, operating systems use **scheduling algorithms** that determine which process receives CPU time and when. These algorithms ensure that tasks are handled efficiently while maintaining responsiveness for active user applications.

Memory management is another essential responsibility of the operating system. Modern systems rely on techniques such as **virtual memory, paging, and caching** to optimize RAM usage and allow more applications to run than the physical memory alone could support.

Understanding how CPU scheduling and memory management work provides insight into system performance and helps developers build applications that operate efficiently within these constraints.

## System Resource Monitoring Program

The Python program developed for this module uses the **psutil** library to retrieve and analyze information about system CPU and memory resources.

### Functions That Retrieve the Information

The program contains three functions designed to collect information about system hardware resources.
