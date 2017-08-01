package com.adventurer.gameobjects;

import com.adventurer.data.Coordinate;
import com.adventurer.enumerations.SpriteType;

public class Gold extends Item {

	private int amount = 0;
	
	public Gold(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int amount) {
		super(worldPos, tilePos, spritetype, "Gold");
		this.amount = amount;
	}

	public int getAmount() { return amount; }
}
