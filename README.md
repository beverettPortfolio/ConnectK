# ConnectK

DerpAI, my Connect K AI, was a project for ICS171, an AI class I took in Fall Quarter 2015. Since then, I have added some small improvements.

Connect K is a general form of connect 4 that can have any board width, height, winning length, and gravity. For example, default Connect 4 is width 7, height 6, winning length 4, with gravity on. Tic-tac-toe is width 3, height 3, winning length 3, gravity off.

My AI is implemented using an iterative deepening minimax search with alpha-beta pruning. The main class is DerpAI, which extends the CKPlayer class that was provided with the project.

In addition, this project contains unit tests for the evaluation and minimax search functions. These automated tests were invaluable while implementing and debugging the algorithms. At one point, when I was playing tic-tac-toe against my AI to manually test it, I found a situation in which the AI made an incorrect move. I simply made a unit test using that board state and the expected correct move, then debugged my code until the test passed. This test case is named "Error State" in the test file MaxMoveTest.java.

At the end of the quarter, the class had a tournament of all submitted AI's. DerpAI placed in the top 20% in the class and defeated all three benchmark AI's used for grading. My AI's performance earned me 18% extra credit for the project.

To run the AI, run ConnectK.jar (which was provided by the professor), click file->new game, and add DerpAI.class from the bin folder. There are also three example AI of varying competence that were provided by the professor and can be run against my AI.
