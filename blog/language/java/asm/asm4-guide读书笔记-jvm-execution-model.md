## Execution Model
* code executes inside a thread
* each frame represent a method invocation, each time a method is invoked, a new frame is pushed on the _current thread's_ execution stack, and pop out if the method invocation finished.
* each frame contains two parts:
    + a local variables
    variables can be accessed by their index in random order.
    + operand stack, the value used as operand by bytecode instructions.
    don't confuse the _operand stack_ and thread's _execution stack_, they are in different scope, _operand stack_ is in the frame, but the _execution stack_ is in the thread.
    + the size of them are computed at compile time and stored along with the bytecode instructions.
    + Question: How do the local variables and operand stack initialised?
