package spellbound.effects;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import spellbound.core.Logic;
import spellbound.core.SB;
import spellbound.enums.EnumSpellType;
import spellbound.util.Coordinates;

public class EffectAdvanceTime extends AbstractEffect
{
	@Override
	public String getSpellDisplayName() 
	{
		return "Advance Time";
	}

	@Override
	public void doSpellEffect(EntityPlayer caster) 
	{
		long newWorldTime = caster.worldObj.getWorldTime() + (SB.rand.nextInt(5000) + 5000);

		if (!caster.worldObj.isRemote)
		{
			caster.worldObj.setWorldTime(newWorldTime);

			for (Coordinates c : Logic.getNearbyBlocks(caster.worldObj, (int)caster.posX, (int)caster.posY, (int)caster.posZ, 25, Block.crops.blockID))
			{
				caster.worldObj.setBlockMetadataWithNotify(c.x, c.y, c.z, 7, 2);
			}

			for (Coordinates c : Logic.getNearbyBlocks(caster.worldObj, (int)caster.posX, (int)caster.posY, (int)caster.posZ, 25, Block.carrot.blockID))
			{
				caster.worldObj.setBlockMetadataWithNotify(c.x, c.y, c.z, 7, 2);
			}

			for (Coordinates c : Logic.getNearbyBlocks(caster.worldObj, (int)caster.posX, (int)caster.posY, (int)caster.posZ, 25, Block.potato.blockID))
			{
				caster.worldObj.setBlockMetadataWithNotify(c.x, c.y, c.z, 7, 2);
			}

			for (Coordinates c : Logic.getNearbyBlocks(caster.worldObj, (int)caster.posX, (int)caster.posY, (int)caster.posZ, 25, Block.melonStem.blockID))
			{
				caster.worldObj.setBlockMetadataWithNotify(c.x, c.y, c.z, 7, 2);
			}

			for (Coordinates c : Logic.getNearbyBlocks(caster.worldObj, (int)caster.posX, (int)caster.posY, (int)caster.posZ, 25, Block.pumpkinStem.blockID))
			{
				caster.worldObj.setBlockMetadataWithNotify(c.x, c.y, c.z, 7, 2);
			}
		}
	}

	@Override
	public void updateSpellEffect() 
	{

	}

	@Override
	public EnumSpellType getSpellType() 
	{
		return EnumSpellType.SELF;
	}

	@Override
	public void doSpellTargetEffect(World worldObj, int posX, int posY, int posZ, EntityLivingBase entityHit) {
		// TODO Auto-generated method stub

	}
}
