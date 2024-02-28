<p><strong>MainWindowController.java</strong>
    <p>- Controller inserts all programs, defined in static variables
 of the "Command" class derivatives (manual, if we want a new program,
 we need to create a new class derivative and add it to the 
 ObservableList)<br>
    <p>- The user can select the program they want, and start a new
instance by pressing the appropiate button. On click, the side
window is instantiated, the "IStatement" created and referenced
in the ListView is passed to the side window, and a manual 
initialization is invoked.<br>
<br>

<p><strong>SideWindowController.java</strong>
    <p>- This class holds a controller for the statement we
want to execute. The manual initialization function calls 2 
refresh functions, meant to display new information to the GUI,
and a startup function, meant to initialize the controller for
the statement.<br>
    <p>- The startup function will create 1 StackADT for the
execution stack, 2 DictionaryADTs, for the symbol table and the
file table, and 1 ListADT, for the output. All of these, alongside
the statement passed from the main window will create a new 
program state. (In the program state, the stack will get either
the full compound statement, or the decomposed version, and
a new HeapADT object will be instantiated, representing the
heap memory for the whole program.)<br>
    <p>- A new Memory Repository is created, in which we add
the program state<br>
    <p>- A new Application Controller is created, with the created
repository as a parameter. The executor service is also 
instantiated, with at most 2 threads per application.<br>
    <p>- The startup function will return the created Application
Controller, all the refresh functions will be called, and the user
will be capable to operate on the launched program.<br>
<br>
