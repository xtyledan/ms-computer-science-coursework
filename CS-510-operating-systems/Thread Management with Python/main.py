import threading

def first_function():
    """
    Displays a unique message identifying this function.
    Executed inside its own thread.
    """
    print("first_function: Executing task in Thread One.")


def second_function():
    """
    Displays a different unique message identifying this function.
    Executed inside its own thread.
    """
    print("second_function: Running independent process in Thread Two.")


def main():
    """
    Creates and runs two threads, each calling a separate function.
    """

    # Create thread objects with target functions
    thread_one = threading.Thread(target=first_function)
    thread_two = threading.Thread(target=second_function)

    # Start the threads
    thread_one.start()
    thread_two.start()

    # Wait for both threads to complete
    thread_one.join()
    thread_two.join()


if __name__ == "__main__":
    # Entry point of the program
    main()