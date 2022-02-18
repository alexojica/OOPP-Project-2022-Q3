## Must have (Basic):
* As a system, one game must contain 20 questions.
* As a system, each question must have 3 distinct answers
* As a player I must receive points for each question based on whether the question was answered correctly.
* As a player I must be able to play single player games.
* As a player, I must have limited time to answer each question (10 seconds)
* As a player I must be able to quit the game whenever I want to
* As a system I must have enough different questions so the player engagement is preserved
* As a system, the questions and their order must be randomly selected 
* (NFR) As a system, the questions and highscores must be stored in a database
* (NFR) As a system, the client will communicate with the server during a game

## Should have (Complete):
* As a system I should have support for multiplayer games
* As a player, I should be able to see the all times-high leaderboard after the end of a solo game
* As a system, I should support multiple online games running in parallel
* As a player, I should be able to see the leaderboard of an on-going online game after every 10 questions (2 leaderboards per game. 1 at the middle, 1 at the end of the game)
* (NFR*) All the players should connect to one localhost/ip that created the room
* (NFR*) As a player I want multiplayer games to be synchronous.
* As a player, I should be able to join a lobby of on-line players, and start the game whenever the lobby is full
* As a player I should have 1 main lobby for multiplayer games, in which any player can start the game, which will then be started with all the players that are currently in that lobby.
* As a player, I must be able to see other people’s names and points in the on-going game
* As a player I should have a unique name in the game
* As a system, I want the same activity not to be shown twice in the same game
* As a player I should see my current score at all times during the game.
* As a system I should have questions that contain pictures or other visual material; Not text only.
* As a player I should be able to use 3 power-ups. (Jokers) i.e: double points, reduced time for others, eliminating answers
* As a player I should have power-ups available for usage at any time during a question
* As a system, multiple power-ups should be able to be used on the same question
* As a system, powerups should each only be used once per game.
* As a player, I should be able to freely choose a name for other players to see in game
* As a system, the questions should have different formats such as multiple choice, comparison and estimation


## Could have (Bonus):
* As a player I want an extra fourth power-up.
* As a player I want to be able to communicate with the other players so the game becomes more alive and fun (emoji/emotes that can be sent to all other players) 
* As a system, I want the questions to have an appropriate difficulty level in order to keep the engagement high (avoid too simple or to too complex questions/answers combinations)
* As a player, I want to be notified when a player disconnects
* As a player, I want to have the choice to join the lobby without inputting my name again, after each game has ended
* As a player I want points to be influenced by how quickly the question was answered.
* As a system, I want players to be kicked after they did not answer the last X questions. (X to be decided.). They could be warned as well.
* As a player I want to be able to create private lobbies (“rooms”) with invite links or tokens to share to my friends
* As a party leader, I want to be able to kick somebody out
* As a party leader, I want to be able to start the game faster without the need for the maximum capacity of players (10)
* As a party leader, I want to be the only one that can start a game
* As a party leader, I want to choose the number of questions that will exists in that match
* As a player, I want to see what percentage of the total possible points I scored.
* As a player I want to be able to discover some hidden easter eggs to make the game feel like there's more to it than just the text-based nature of it
* As a player I could have a customisable image/icon/figure which represents me and is visible to other players in the game to add a sense of customizability and personalisation to the game
* As a player I want to be able to score more points through a correct answer streak.


## Will not have:
* As a player I do not want to enter my information and have to authenticate every time I want to play
* As a player, I do not want to reconnect back to a game, after I drop out of it. Also, all personal progress of that game is lost
* As a system I do not want an explicit tutorial showing players how the game works. The game should be self-explanatory.


NFR = Non Functional Requirements
