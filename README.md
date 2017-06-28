# Adventurer - Roguelite Game
Adventurer is a roguelite game, that uses realtime tile-based movement system. It is not turn-based and therefore it is not a rogueLIKE game.

## Tech
The game uses simple 2D game engine written in Java, that is based on Notch's work, but also has my own code and some snippets from other sites. Every snippet, that is not mine, has a link to the original owner.

### Features
Engine supports these features:
* Spritesheet sprite drawing (16x16)
* Rendering queue
* ...

At this moment the game supports these features:
* Dungeon generation from predefined 2d char arrays.
* Line of sight calculations: usees Bresenham's line algorithm.
* Enemies use A*-pathing algorithm.
* Enemy types: some enemies can shoot projectiles, some are only melee and some create bombs on death.
* Tile based system: tile can have actor, item and multiple vanity items on it.
* Tile types: Traps (gas & projectile), doors (unlocked & locked), portals, walls (undestructible & destructible), floor.
* Inventory-system: keys (normal & diamond), bombs, projectiles
* Session files & permanent save files
* Persistency: what ever the player gained from a single session can be saved into a persistent save file (such as diamond keys).
* ... 

## TODO
There is a lot of work to do here. At the moment multiple bugs and crashes are the number one on my TODO-list.

Features that are necessary in the future:
* Some sort of random dungeon generation
* Better GUI
* Main menu
* Bosses
* ...

## How to Contribute
See contributing.md for more info, but in short you can submit new ideas and features to me via twitter (@Baserfaz). I'm more than glad to read your ideas.

## Contributors
* Heikki Heiskanen (Baserfaz) - Initial work
