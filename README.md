# SurvivalGames
The Survival Games game for Mineswine.

This game will include four stages.

             /**
             * Waiting stage.
             * @info this stage is the lobby portion of the minigame,
             * players cannot PVP, PVE, or alter any blocks. Players
             * also will vote for the map that they would like to
             * play on in the waiting stage.
             */
             
             /**
             * Started stage.
             * @info this stage is a transitional stage between the lobby and the progress.
             * It sets up the player handlers such as PVP, PVE, and sends them to the area.
             */
             
             /**
             * Progress stage.
             * @info this stage is the meat of the game, here is where the players actually
             * try to stay alive for as long as they can and kill other players during this
             * stage. If the stage has not expired, but the player count gets to 5 - the
             * death match automatically starts; likewise if the stage expires and there are
             * more than 5 players left in the game, it will start a death match anyways.
             */
             
             /**
             * Death match stage.
             * @info this stage is the finale of the game. Here is where players are in-closed
             * and forced to fight to the death. It will last for approximately 2 minutes.
             * If there is not a winner by the last 20 seconds, poison will be applied to all
             * players to "motivate" them to eliminate all opponents.
             */
             
             The setup of the game includes of a few main functions. The first function is the algorithm that loops through all of the
             loaded chunks in the map, and searches for sign in the chunks. If the sign contains a string starting with "{sg:}", then it              registers it as a location sign and puts it into memory. These location signs can vary from functionality to player spawns. 
             For instance, the plugin registeres player spawn locations by detecting the signs that say "{sg:_loc}", and loads them into              memory so that when the game starts, the plugin knows where to put the players depending on the order of the player cache.
             
             The stats system of SurvivalGames is reletively simple. When a player does something that credits or removes their account
             with a statistic, it simply uses the connection to the mysql database via the main game instance; grabs the statistic that              currently exists for that player (UUID) and the desired field and simply modifies it depending on whether or not the goal
             is to either add or subtract the statistic. As of now the only statistics that exist are Kills, Deaths, Games Played, Games              Won, Total Chests Opened, Tier I Chests Opened, Tier II Chests Opened, and tokens. Stats are currently viewable via /stats,
             and functionality-wise you are only able to view your own personal statistics and not other players statistics (Being
             worked on).
             
             Tokens are the main currency of the game mode. Tokens are obtained simply by playing games until the end of it. Tokens can              also be obtained by killing other players, or even winning the game. Winning the game is very simple and self explanatory
             as you must be the last surviving player or you must eliminate all of the other players. Tokens will soon be able to buy
             in game vanity cosmetics such as arrow particle trails.

             Rating is the system in how we score each player and their performance overall in all of the games that they play.
             A player that has 2,000 rating or higher, will recieve a purple cape made out of particles to where in the lobby
             to show that they are very skilled in what they do and shows their devotion and commitment to successfulness on                          SurvivalGames.  
             
             CONFIGURATION??
             The only thing that requires configuration, is the location for the center of the map, as well as the lobby of the game.

             SoonTM??
             Need to make the cuboid points for where the crates are checked and generated for loot.
