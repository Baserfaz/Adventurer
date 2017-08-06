package com.adventurer.data;

public class PredefinedMaps {

	/*	Char to GameObject-map  
	 *  W -> wall
	 *  # -> outer wall 
	 *  @ -> player spawn point
	 *  . -> floor
	 *  d -> door
	 *  D -> diamond door
	 *  L -> locked door
	 *  P -> portal to dungeon
	 *  E -> exit from dungeon
	 *  c -> chest
	 *  l -> locked chest
	 */
	
	private static final char[][] lobbyMap = new char[][] {
		{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
		{ '#', '.', '.', '.', 'W', 'c', 'c', 'c', 'W', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', 'W', '.', '@', '.', 'W', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', 'W', '.', '.', '.', 'W', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', 'W', 'd', 'W', 'd', 'W', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '#' },
		{ '#', 'W', 'd', 'W', 'W', 'W', 'D', 'W', 'W', 'W', 'D', 'W', '#' },
		{ '#', '.', '.', '.', 'W', '.', '.', '.', 'W', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', 'W', '.', '.', '.', 'W', '.', '.', '.', '#' },
		{ '#', '.', 'P', '.', 'W', '.', '.', '.', 'W', '.', '.', '.', '#' },
		{ '#', '.', '.', '.', 'W', '.', '.', '.', 'W', '.', '.', '.', '#' },
		{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
	};

	public static char[][] GetLobby() { return lobbyMap; }
}
