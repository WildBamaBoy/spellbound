package spellbound.spells;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import spellbound.enums.EnumSpellType;

public class SpellHaste extends AbstractSpell
{
	@Override
	public String getSpellDisplayName() 
	{
		return "Haste";
	}

	@Override
	public void doSpellCasterEffect(EntityPlayer caster) 
	{
		caster.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 1200));
		caster.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 1200));
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
