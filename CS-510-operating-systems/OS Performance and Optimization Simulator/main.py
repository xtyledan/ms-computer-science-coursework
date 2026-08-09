"""
    Class:   CS-510
    Author:  <Tyler Daniels>
    Date:    <4/5/2026>

    Description:  Each function that needs to be completed has a comment at the top with
                  TODO written in it with instructions.

                  Within the function is a section with the comment #TODO where you will
                  insert your code as per the instructions.
"""  
import os
from datetime import datetime
import psutil  # requires pip install
import sys
import threading

"""
  Three provided Utility functions to use
"""
def printBlankLines(lines: int):
    for i in range(lines):
        print("")

def printMsg1(num):
    current_thread = threading.current_thread()
    print(f"Thread 1 executing on name={current_thread.name}, id={current_thread.ident}, native_id={current_thread.native_id}")
    print("Thread 1 cubed: {}" .format(num * num * num))
    print(f"Thread 1 finished on name={current_thread.name}, id={current_thread.ident}")


def printMsg2(num):
    current_thread = threading.current_thread()
    print(f"Thread 2 executing on name={current_thread.name}, id={current_thread.ident}, native_id={current_thread.native_id}")
    print("Thread 2 squared: {}" .format(num * num))
    print(f"Thread 2 finished on name={current_thread.name}, id={current_thread.ident}")


"""
   TODO This function will display information about a file, size and file information.
   You can provide your own or use the projecttwo.txt file.

   Use psutil to get initial file information.
"""
def getFileDiskUsageStatistics() -> None:
    print("Getting Disk Statistics")
    file_name = "./projecttwo.txt"

    target_file = file_name if os.path.exists(file_name) else __file__
    file_stats = os.stat(target_file)
    disk_usage = psutil.disk_usage(os.path.dirname(os.path.abspath(target_file)) or ".")
    created_time = getattr(file_stats, "st_birthtime", file_stats.st_ctime)

    print(f"File Name: {target_file}")
    print(f"File Size: {file_stats.st_size} bytes")
    print(f"Last Modified: {datetime.fromtimestamp(file_stats.st_mtime)}")
    print(f"Last Accessed: {datetime.fromtimestamp(file_stats.st_atime)}")
    print(f"Created: {datetime.fromtimestamp(created_time)}")
    print(f"Disk Total: {disk_usage.total} bytes")
    print(f"Disk Used: {disk_usage.used} bytes")
    print(f"Disk Free: {disk_usage.free} bytes")
    print(f"Disk Percent Used: {disk_usage.percent}%")

    printBlankLines(2)

"""
   TODO This should use psutil to retrieve standard and 
   virtual memory statistics
"""
def getMemoryStatistics() -> None:
    print("Getting Memory Statistics")

    virtual_memory = psutil.virtual_memory()
    swap_memory = psutil.swap_memory()

    print(f"Total Memory: {virtual_memory.total} bytes")
    print(f"Available Memory: {virtual_memory.available} bytes")
    print(f"Used Memory: {virtual_memory.used} bytes")
    print(f"Memory Percent Used: {virtual_memory.percent}%")
    print(f"Swap Total: {swap_memory.total} bytes")
    print(f"Swap Used: {swap_memory.used} bytes")
    print(f"Swap Free: {swap_memory.free} bytes")
    print(f"Swap Percent Used: {swap_memory.percent}%")

    printBlankLines(2)

"""
   TODO This should use psutil to retrieve CPU statistics, including
   information on processes.
"""
def getCpuStatistics() -> None:
    print("Getting CPU Statistics")
    
    print(f"CPU Count (logical): {psutil.cpu_count(logical=True)}")
    print(f"CPU Count (physical): {psutil.cpu_count(logical=False)}")
    print(f"CPU Percent: {psutil.cpu_percent(interval=1)}%")
    print(f"CPU Times: {psutil.cpu_times()}")
    print(f"Process Count: {len(psutil.pids())}")
    current_process = psutil.Process(os.getpid())
    print(f"Current Process ID: {current_process.pid}")
    print(f"Current Process Name: {current_process.name()}")
    print(f"Current Process Status: {current_process.status()}")

    printBlankLines(2)


"""
   TODO This function will show multi threading capabilities.

   Two threads should be created.
    
   One used to call the function "printMsg1" provided above.

   A second thread should call the function "printMsg2" provided above.
"""
def showThreadingExample() -> None:
    print("Demonstrating Threading")

    print("Creating Thread 1")
    thread1 = threading.Thread(target=printMsg1, args=(3,), name="Thread-1")
    print(f"Created Thread 1 object: name={thread1.name}, id={thread1.ident}")

    print("Creating Thread 2")
    thread2 = threading.Thread(target=printMsg2, args=(4,), name="Thread-2")
    print(f"Created Thread 2 object: name={thread2.name}, id={thread2.ident}")

    thread1.start()
    print(f"Thread 1 started: name={thread1.name}, id={thread1.ident}, native_id={thread1.native_id}")
    thread2.start()
    print(f"Thread 2 started: name={thread2.name}, id={thread2.ident}, native_id={thread2.native_id}")

    thread1.join()
    print(f"Thread 1 destroyed after join: name={thread1.name}, id={thread1.ident}")
    thread2.join()
    print(f"Thread 2 destroyed after join: name={thread2.name}, id={thread2.ident}")

    print("Done With Threading!")

    printBlankLines(2)

"""
   TODO This function shows system error handling.

   Since out of memory, or out of disk space errors are difficult to create,
   we will use a divide by zero error and show the error handling being executed.
"""
def showErrorHandling() -> None:
    print("Demonstrating Error Handling")
    try:
       
       res = 1 / 0
    
    except ZeroDivisionError:
        print("You can't divide by zero!")
        
    except MemoryError:
        print("Memory Error!")
        
    else:
        print("Result is", res)
        
    finally:
        print("Execution complete.")

    printBlankLines(2)

"""
   Main function, does not require modification.

   This calls the specific functions.
"""
def main() -> int:
    print("Starting Program")
    print("=============================")
   
    getFileDiskUsageStatistics()   
    
    getCpuStatistics()
    
    getMemoryStatistics()

    showThreadingExample()

    showErrorHandling()


if __name__ == '__main__':
    sys.exit(main()) 