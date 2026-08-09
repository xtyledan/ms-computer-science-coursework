<h1 align="center"> Exploring System Resources with Python</h1>

## Learning Objectives

By the end of this module, the following objectives are demonstrated:

- Describe how system resource monitoring reflects operating system behavior  
- Explain how CPU usage and core count relate to system performance  
- Interpret memory statistics including total, used, and available memory  
- Analyze how operating systems allocate CPU time and memory resources  

## Module Overview

Operating systems manage system resources such as CPU time and memory to ensure that multiple programs can run efficiently at the same time. Many users interact with several applications at once without noticing the complex resource management happening behind the scenes.

Monitoring system resources provides direct insight into how the operating system is functioning. CPU usage indicates how actively the processor is being utilized, while memory statistics show how system memory is allocated and used. These measurements help illustrate how efficiently the system is operating.

This module focuses on using Python to retrieve and interpret system-level information. The program interacts with the operating system to collect real-time data about CPU and memory usage, providing a practical view of how resource management works in real environments.

## System Resource Monitoring Program

The Python program developed for this module uses the **psutil** library to retrieve and analyze information about system CPU and memory resources.

### How the Code Retrieves System Information

The program contains three functions designed to collect information about system hardware resources.

- **get_cpu_usage()**  
  Uses `psutil.cpu_percent(interval=1)` to measure CPU activity over a one second interval. This produces a more accurate representation of processor usage rather than an instant reading.

- **get_cpu_count()**  
  Uses `psutil.cpu_count()` to return the number of logical CPU cores available. This reflects how many processing threads the operating system can utilize.

- **get_memory_stats()**  
  Uses `psutil.virtual_memory()` to retrieve system memory statistics. The values are converted from bytes into gigabytes for readability. The function returns:
  - Total memory  
  - Used memory  
  - Available memory  

All functions focus on collecting and returning data, while formatting and displaying the output is handled separately.

## Running the Program

1. Install Python.  
2. Open a terminal (macOS/Linux) or Command Prompt/PowerShell (Windows).  
3. Navigate to the folder containing the program using the `cd` command.  
4. Run the program:
   ```bash
   python main.py