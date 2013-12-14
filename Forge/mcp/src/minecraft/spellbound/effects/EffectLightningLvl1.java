package spellbound.effects;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import spellbound.entity.EntityTargetSpell;
import spellbound.enums.EnumSpellType;

public class EffectLightningLvl1 extends AbstractEffect
{
	@Override
	public String getSpellDisplayName() 
	{
		return "Tazer";
	}

	@Override
	public void doSpellEffect(EntityPlayer caster) 
	{
		caster.inventory.consumeInventoryItem(caster.inventory.currentItem);
		caster.worldObj.playSoundAtEntity(caster, "mob.ghast.fireball", 1.0F, 1.0F);
		
		Vec3 vec = caster.getLookVec();
		EntityTargetSpell spell = new EntityTargetSpell(caster.worldObj, this);
		spell.setPosition(caster.posX + vec.xCoord * 5, caster.posY + 1 + vec.yCoord * 5, caster.posZ + vec.zCoord * 5);
		
		spell.accelerationX = vec.xCoord * 0.3;
		spell.accelerationY = vec.yCoord * 0.3;
		spell.accelerationZ = vec.zCoord * 0.3;
		
		caster.worldObj.spawnEntityInWorld(spell);
	}

	@Override
	public void updateSpellEffect() 
	{
		
	}
	
	@Override
	public EnumSpellType getSpellType() 
	{
		return EnumSpellType.TARGET;
	}

	@Override
	public void doSpellTargetEffect(World worldObj, int posX, int posY, int posZ, EntityLivingBase entityHit) {
		// TODO Auto-generated method stub
		
	}
}
