package spellbound.effects;

import net.minecraft.entity.player.EntityPlayer;
import spellbound.enums.EnumSpellType;

public class EffectFireLvl3 extends AbstractEffect
{
	@Override
	public String getSpellDisplayName() 
	{
		return "Scorcher";
	}

	@Override
	public void doSpellEffect(EntityPlayer caster) 
	{
		
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
}
