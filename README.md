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
             
             CONFIGURATION??
             The only thing that requires configuration, is the location for the center of the map, as well as the lobby of the game.

             SoonTM??
             Need to make the cuboid points for where the crates are checked and generated for loot.
