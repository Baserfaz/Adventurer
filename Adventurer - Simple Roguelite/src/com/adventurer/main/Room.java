package com.adventurer.main;

import java.util.List;

import com.adventurer.enumerations.RoomType;
import com.adventurer.gameobjects.Tile;

public class Room {

	private int roomWidth;
	private int roomHeight;
	private Coordinate roomPosition;
	private List<Tile> tiles;
	private RoomType roomType;
	
	public Room(int roomwidth, int roomheight, Coordinate roompos, List<Tile> tiles, RoomType roomType) {
		this.setRoomWidth(roomwidth);
		this.setRoomHeight(roomheight);
		this.setRoomPosition(roompos);
		this.setTiles(tiles);
		this.setRoomType(roomType);
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

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}
}
