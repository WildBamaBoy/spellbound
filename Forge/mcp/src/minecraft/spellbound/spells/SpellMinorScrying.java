package spellbound.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import spellbound.entity.EntityAllSeeingEye;
import spellbound.enums.EnumItemInUseTime;
import spellbound.enums.EnumSpellType;

public class SpellMinorScrying extends AbstractSpell
{
	@Override
	public String getSpellDisplayName() 
	{
		return "Minor Scrying";
	}

	@Override
	public void doSpellCasterEffect(EntityPlayer caster) 
	{
		if (!caster.worldObj.isRemote)
		{
			EntityAllSeeingEye eye = new EntityAllSeeingEye(caster.worldObj, caster.posX, caster.posY, caster.posZ);
			eye.setPosition(caster.posX, caster.posY, caster.posZ);
			caster.worldObj.spawnEntityInWorld(eye);
		}
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
	
	@Override
	public EnumItemInUseTime getSpellDuration() 
	{
		return EnumItemInUseTime.ONE_SECOND;
	}
}
