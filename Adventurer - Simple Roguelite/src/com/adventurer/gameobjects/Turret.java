package com.adventurer.gameobjects;

import java.awt.Graphics;

import com.adventurer.Utilities.Renderer;
import com.adventurer.Utilities.Util;
import com.adventurer.enumerations.Direction;
import com.adventurer.enumerations.SpriteType;
import com.adventurer.main.Coordinate;
import com.adventurer.main.SpriteCreator;
import com.adventurer.main.World;

public class Turret extends Actor {
	
	private int shootCooldown = 2500;
	private long shootTimer;
	SpriteType projSpriteType;
	
	public Turret(Coordinate worldPos, Coordinate tilePos, SpriteType spritetype, int damage) {
		super(worldPos, tilePos, spritetype, 500, damage);
		
		this.shootTimer = System.currentTimeMillis() + shootCooldown + Util.GetRandomInteger(100, 500);
		this.projSpriteType = SpriteType.Arrow01;
		this.lookDir = Direction.values()[Util.GetRandomInteger(0, 4)];
		
		switch(lookDir) {
		case East:
			this.SetSprite(SpriteCreator.instance.FlipSpriteHorizontally(SpriteCreator.instance.CreateSprite(SpriteType.ArrowTurretWest)));
			this.SetTintedSprite(null);
			break;
		case North:
			this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.ArrowTurretNorth));
			this.SetTintedSprite(null);
			break;
		case South:
			this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.ArrowTurretSouth));
			this.SetTintedSprite(null);
			break;
		case West:
			this.SetSprite(SpriteCreator.instance.CreateSprite(SpriteType.ArrowTurretWest));
			this.SetTintedSprite(null);
			break;
		default:
			System.out.println("NOT VALID DIRECTION!");
			new Exception().printStackTrace();
			System.exit(1);
			break;
		}
	}
	
	public void tick() {
		
		if(this.GetHealth().isDead()) {
			
			OnDeath(World.instance.GetTileAtPosition(this.GetTilePosition()));
			
		} else {
			
			if(shootTimer < System.currentTimeMillis()) {
				shoot();
				shootTimer = System.currentTimeMillis() + shootCooldown;
			}
			
		}
	}
	
	public void render(Graphics g) {
		if(hidden == false) {
			Renderer.RenderSprite(sprite, this.GetWorldPosition(), g);
			renderDirectionArrow(g);
		} else if(discovered == true && hidden == true) {
			if(tintedSprite == null) { tintedSprite = Util.tint(sprite, true); }
			Renderer.RenderSprite(tintedSprite, this.GetWorldPosition(), g);
		}
	} 
	
	public void shoot() {
		//Tile currentTile = World.instance.GetTileAtPosition(this.GetTilePosition());
		new Projectile(this.GetWorldPosition(), this.GetTilePosition(), projSpriteType, damage, lookDir);
	}
}
