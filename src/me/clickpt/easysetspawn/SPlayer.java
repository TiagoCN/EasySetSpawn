package me.clickpt.easysetspawn;

import org.bukkit.scheduler.BukkitTask;

public class SPlayer {
	
	private BukkitTask task;
	private int i;
	private int startX;
	private int startY;
	private int startZ;
	
	protected SPlayer(BukkitTask task, int startX, int startY, int startZ) {
		this.task = task;
		this.i = 0;
		this.startX = startX;
		this.startY = startY;
		this.startZ = startZ;
	}
	
	protected SPlayer setI(int i) {
		this.i = i;
		
		return this;
	}
	
	protected BukkitTask getTask() {
		return task;
	}
	
	protected int getI() {
		return i;
	}
	
	protected int getStartX() {
		return startX;
	}
	
	protected int getStartY() {
		return startY;
	}
	
	protected int getStartZ() {
		return startZ;
	}
	
}
