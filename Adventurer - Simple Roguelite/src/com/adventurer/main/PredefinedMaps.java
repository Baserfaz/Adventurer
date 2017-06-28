package com.adventurer.main;

import com.adventurer.enumerations.RoomType;

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
	 */
	
	private static final char[][] lobbyMap = new char[][] {
		{ '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#' },
		{ '#', '.', '.', '.', 'W', '.', '.', '.', 'W', '.', '.', '.', '#' },
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
	
	private static final char[][] small01 = new char[][] {
		{ '#', '#', '#', '#', '#', '#', '#' },
		{ '#', '@', '.', 'd', '.', '.', '#' },
		{ '#', '.', '.', 'W', '.', '.', '#' },
		{ '#', 'W', 'd', 'W', 'd', 'W', '#' },
		{ '#', '.', '.', 'W', '.', '.', '#' },
		{ '#', '.', '.', 'W', '.', 'E', '#' },
		{ '#', '#', '#', '#', '#', '#', '#' },
	};
	
	private static final char[][] small02 = new char[][] {
		{ '#', '#', '#', '#', '#', '#', '#' },
		{ '#', 'E', 'W', '.', '.', '@', '#' },
		{ '#', '.', 'W', '.', '.', '.', '#' },
		{ '#', 'd', 'W', 'W', 'd', 'W', '#' },
		{ '#', '.', '.', 'W', '.', '.', '#' },
		{ '#', '.', '.', 'd', '.', '.', '#' },
		{ '#', '#', '#', '#', '#', '#', '#' },
	};
	
	public static char[][] GetRoom(RoomType roomType) {
		
		// TODO: create arrays to hold rooms with different roomTypes
		// and then get a random room.
		
		System.out.println(roomType);
		
		return small02;
	}
	
	public static char[][] GetLobby() {
		return lobbyMap;
	}
	
}
