This program is around 85% complete. My application is multi-threaded
allowing two clients to connect, message each other, and play a game of battleship.
Multiple clients can also connect and play a separate game of battleship and chat.
I was not able to implement the observer design pattern to the buttons, instead I
added an action listener to my buttons. When a player has won the client
is asked if they would like to play a game, if both clients respond yes, the 
grids seem to reset however the opponent's grid is still stuck to the previous game.
If one player says no that client is supposed to disconnect however both clients
seem to disconnect resulting in the client having to reconnect again. When both
both clients say no both connections are disconnected.