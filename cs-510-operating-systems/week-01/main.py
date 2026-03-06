import psutil

def display_process_info():
    """
    Retrieves and displays information about all active system processes.
    Output includes PID, process name, memory usage %, and CPU usage %.
    """

    # Define column headers with consistent spacing
    header = f"{'PID':<10} {'Name':<25} {'Memory %':<10} {'CPU %':<10}"
    print(header)
    print("-" * len(header))
    
    # Iterate through all running processes and extract relevant info
    for proc in psutil.process_iter(['pid', 'name', 'memory_percent', 'cpu_percent']):
        try:
            ## get the PID
            pid = proc.info['pid']
            ## get the name
            name = proc.info['name']
            ## get the memory_percent
            mem = proc.info['memory_percent']
            ## get the cpu_percent
            cpu = proc.info['cpu_percent']

            # Print formatted process details retrieved from above
            print(f"{pid:<10} {name:<25} {mem:<10.2f} {cpu:<10.2f}")

        except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
            # Skip processes that can't be accessed or no longer exists
            continue

if __name__ == "__main__":
    # Entry point for the script
    display_process_info()