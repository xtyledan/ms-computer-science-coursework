"""
A program that prints basic CPU and memory details
using psutil library.

The display part is provided, just fill out the first
three functions

Modify as you like, experimentation is fun!
"""

import psutil

def get_cpu_usage():
    """
    Return the current CPU usage percentage.
    
    Returns:
        float: CPU usage as a percentage (0-100)
    """
    return psutil.cpu_percent(interval=1)

def get_cpu_count():
    """
    Return the number of logical CPU cores detected on the system.
    
    Returns:
        int: Number of logical CPU cores
    """
    return psutil.cpu_count()

def get_memory_stats():
    """
    Return the total memory, available memory, and memory used.
    
    All values returned are in gigabytes for readability.
    
    Returns:
        tuple: (total_gb, used_gb, available_gb) - Memory statistics in gigabytes
    """
    BYTES_PER_GB = 1024 ** 3
    
    # Get virtual memory stats from psutil
    memory = psutil.virtual_memory()
    
    # Convert bytes to gigabytes
    total = memory.total / BYTES_PER_GB
    used = memory.used / BYTES_PER_GB
    available = memory.available / BYTES_PER_GB
    
    return total, used, available

def display_resource_report():
    """
    Display a formatted report of CPU and memory information.
    
    Prints a nicely formatted system resource report containing:
    - CPU usage percentage
    - Number of CPU cores
    - Total memory available
    - Current memory usage
    - Available memory
    """
    mem_total, mem_used, mem_available = get_memory_stats()

    print("\n===== System Resource Report =====")
    print(f"CPU Usage: {get_cpu_usage():5.1f}%")
    print(f"CPU Cores: {get_cpu_count()}")
    print(f"Total Memory: {mem_total:5.2f} GB")
    print(f"Memory Usage: {mem_used:5.2f} GB")
    print(f"Memory Available: {mem_available:5.2f} GB")
    print("===================================\n")

def main():
    """
    Program entry point.
    
    Displays the current system resource statistics including
    CPU usage, CPU core count, and memory information.
    """
    display_resource_report()

if __name__ == "__main__":
    main()