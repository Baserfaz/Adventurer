package com.adventurer.main;

import java.util.List;

import com.adventurer.gameobjects.Tile;

public class Room {

	private int roomWidth;
	private int roomHeight;
	private Coordinate roomPosition;
	private List<Tile> tiles;
	
	public Room(int roomwidth, int roomheight, Coordinate roompos, List<Tile> tiles) {
		this.setRoomWidth(roomwidth);
		this.setRoomHeight(roomheight);
		this.setRoomPosition(roompos);
		this.setTiles(tiles);
	}

	public int getRoomWidth() {
		return roomWidth;
	}

	public void setRoomWidth(int roomWidth) {
		this.roomWidth = roomWidth;
	}

	public int getRoomHeight() {
		return roomHeight;
	}

	public void setRoomHeight(int roomHeight) {
		this.roomHeight = roomHeight;
	}

	public Coordinate getRoomPosition() {
		return roomPosition;
	}

	public void setRoomPosition(Coordinate roomPosition) {
		this.roomPosition = roomPosition;
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(List<Tile> tiles) {
		this.tiles = tiles;
	}
}
