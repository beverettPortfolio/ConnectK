# ConnectK

DerpAI, my Connect K AI, was written for my artificial intelligence class in Fall Quarter 2015.

Connect K is a general form of connect 4 that can have any board width, height, winning length, and gravity. My AI is implemented using an iterative deepening minimax search with alpha-beta pruning.

The main class is DerpAI in src/, and the rest of my code is in src/com/beverett/derpai.

In addition, this project contains unit tests for the evaluation and minimax search functions in src/test. These automated tests were invaluable while implementing and debugging the algorithms.

To run the AI, run ConnectK.jar (which was provided by the professor), click file->new game, and add DerpAI.class from the bin folder.