package spellbound.core;

import net.minecraft.block.Block;

public final class Constants 
{
	public static final String VERSION = "ModJam 3 Beta";
	
	public static final int[] CROP_IDS = new int[]
			{
		Block.crops.blockID, 
		Block.carrot.blockID, 
		Block.potato.blockID,
		Block.melonStem.blockID, 
		Block.pumpkinStem.blockID
			};

	public static final int[] BLOCKS_SUPPORTING_SNOW = new int[]
			{
		Block.grass.blockID, 
		Block.dirt.blockID, 
		Block.sand.blockID, 
		Block.stone.blockID, 
		Block.cobblestone.blockID
			};

	public static final int RADIUS_ADVANCETIME = 25;
}
