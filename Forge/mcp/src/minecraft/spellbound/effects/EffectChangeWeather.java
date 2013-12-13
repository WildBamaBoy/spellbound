package spellbound.effects;

import net.minecraft.server.MinecraftServer;
import spellbound.enums.EnumSpellType;

public class EffectChangeWeather extends AbstractEffect
{
	@Override
	public String getSpellDisplayName() 
	{
		return "Change Weather";
	}

	@Override
	public void doSpellEffect() 
	{
        MinecraftServer.getServer().worldServers[0].toggleRain();
        MinecraftServer.getServer().worldServers[0].getWorldInfo().setThundering(true);
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
}
