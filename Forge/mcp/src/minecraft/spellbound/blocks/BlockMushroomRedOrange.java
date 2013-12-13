package spellbound.blocks;

import spellbound.effects.AbstractEffect;

public class BlockMushroomRedOrange extends AbstractMushroom
{
	public BlockMushroomRedOrange(int itemId) 
	{
		super(itemId);
	}

	@Override
	public AbstractEffect getMushroomEffect() 
	{
		return null;
	}

	@Override
	public void setName() 
	{
		this.setUnlocalizedName("RED ORANGE PRIMARY");
	}

	@Override
	public void setTexture() 
	{
		this.setTextureName("spellbound:mushroom_redorange");
	}
}
