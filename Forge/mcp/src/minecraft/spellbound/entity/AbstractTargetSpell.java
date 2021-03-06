/**********************************************
 * AbstractTargetSpell.java
 * Copyright (c) 2013 Wild Bama Boy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 **********************************************/

package spellbound.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import spellbound.core.SpellboundCore;
import spellbound.spells.AbstractSpell;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class AbstractTargetSpell extends Entity
{
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private int inTile;
	private int ticksAlive;
	private int ticksInAir;
	private boolean inGround;

	public EntityLivingBase caster;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	protected final AbstractSpell spell;

	/**
	 * Constructor for client rendering.
	 * 
	 * @param 	worldObj	The world to render the object in.
	 */
	public AbstractTargetSpell(World worldObj)
	{
		super(worldObj);
		spell = null;
	}

	public AbstractTargetSpell(EntityPlayer player, AbstractSpell spell) 
	{
		super(player.worldObj);

		final Vec3 vec = player.getLookVec();

		this.caster = player;
		this.spell = spell;
		this.setPosition(player.posX + vec.xCoord * 2, player.posY + 1 + vec.yCoord * 2, player.posZ + vec.zCoord * 2);
		this.accelerationX = vec.xCoord * 0.5;
		this.accelerationY = vec.yCoord * 0.5;
		this.accelerationZ = vec.zCoord * 0.5;

		this.motionX = accelerationX;
		this.motionY = accelerationY;
		this.motionZ = accelerationZ;
	}

	protected abstract String getDisplayParticle();

	protected void onImpact(MovingObjectPosition pos) 
	{
		//Spell will be null when exiting while spell is in progress.
		if (!worldObj.isRemote && spell != null)
		{
			if (pos.entityHit != null && pos.entityHit instanceof EntityLivingBase)
			{
				spell.doSpellTargetEffect(worldObj, pos.blockX, pos.blockY, pos.blockZ, (EntityLivingBase)pos.entityHit);
			}

			else
			{
				spell.doSpellTargetEffect(worldObj, pos.blockX, pos.blockY, pos.blockZ, null);
			}
		}

		setDead();
	}

	@Override
	protected void entityInit() 
	{
		// No init.
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance)
	{
		if (this instanceof EntityMeteor)
		{
			return true;
		}

		else
		{
			double weightedLength = this.boundingBox.getAverageEdgeLength() * 4.0D;
			weightedLength *= 64.0D;
			return distance < weightedLength * weightedLength;
		}
	}

	@Override
	public void onUpdate()
	{
		if (!this.worldObj.isRemote && (this.caster != null && this.caster.isDead || !this.worldObj.blockExists((int)this.posX, (int)this.posY, (int)this.posZ)))
		{
			this.setDead();
		}

		else
		{
			super.onUpdate();

			if (!this.worldObj.isRemote)
			{
				if (this.caster != null && this.getDistanceToEntity(caster) > 150.0D && !(this instanceof EntityMeteor))
				{
					setDead();
					return;
				}
			}

			if (this.inGround)
			{
				int i = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);

				if (i == this.inTile)
				{
					++this.ticksAlive;

					if (this.ticksAlive == 600)
					{
						this.setDead();
					}

					return;
				}

				this.inGround = false;
				this.motionX = accelerationX; //(double)(this.rand.nextFloat() * 0.2F);
				this.motionY = accelerationY; //(double)(this.rand.nextFloat() * 0.2F);
				this.motionZ = accelerationZ; //(double)(this.rand.nextFloat() * 0.2F);
				this.ticksAlive = 0;
				this.ticksInAir = 0;
			}
			else
			{
				++this.ticksInAir;
			}

			Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			MovingObjectPosition movingobjectposition = this.worldObj.clip(vec3, vec31);
			vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
			vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

			if (movingobjectposition != null)
			{
				vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;

			for (int j = 0; j < list.size(); ++j)
			{
				Entity entity1 = (Entity)list.get(j);

				if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual(this.caster) || this.ticksInAir >= 25))
				{
					float f = 0.3F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);

					if (movingobjectposition1 != null)
					{
						double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

						if (d1 < d0 || d0 == 0.0D)
						{
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null)
			{
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if (movingobjectposition != null)
			{
				this.onImpact(movingobjectposition);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;

			for (this.rotationPitch = (float)(Math.atan2((double)f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
			{
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
			{
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F)
			{
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
			{
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f2 = this.getMotionFactor();

			if (this.isInWater())
			{
				for (int k = 0; k < 4; ++k)
				{
					float f3 = 0.25F;
					this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ);
				}

				f2 = 0.8F;
			}

			this.motionX += this.accelerationX;
			this.motionY += this.accelerationY;
			this.motionZ += this.accelerationZ;
			this.motionX *= (double)f2;
			this.motionY *= (double)f2;
			this.motionZ *= (double)f2;

			if (this.worldObj.isRemote)
			{
				for (int i = 0; i < 4; i++)
				{
					double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
					double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
					double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

					this.worldObj.spawnParticle(getDisplayParticle(), this.posX + SpellboundCore.modRandom.nextFloat(), this.posY + SpellboundCore.modRandom.nextFloat(), this.posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
				}
			}

			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	protected float getMotionFactor()
	{
		return 0.95F;
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setShort("xTile", (short)this.xTile);
		par1NBTTagCompound.setShort("yTile", (short)this.yTile);
		par1NBTTagCompound.setShort("zTile", (short)this.zTile);
		par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
		par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
		par1NBTTagCompound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.xTile = par1NBTTagCompound.getShort("xTile");
		this.yTile = par1NBTTagCompound.getShort("yTile");
		this.zTile = par1NBTTagCompound.getShort("zTile");
		this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
		this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

		if (par1NBTTagCompound.hasKey("direction"))
		{
			NBTTagList nbttaglist = par1NBTTagCompound.getTagList("direction");
			this.motionX = ((NBTTagDouble)nbttaglist.tagAt(0)).data;
			this.motionY = ((NBTTagDouble)nbttaglist.tagAt(1)).data;
			this.motionZ = ((NBTTagDouble)nbttaglist.tagAt(2)).data;
		}
		else
		{
			this.setDead();
		}
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public float getCollisionBorderSize()
	{
		return 1.0F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (this.isEntityInvulnerable())
		{
			return false;
		}

		else
		{
			this.setBeenAttacked();

			if (par1DamageSource.getEntity() != null)
			{
				Vec3 vec3 = par1DamageSource.getEntity().getLookVec();

				if (vec3 != null)
				{
					this.motionX = vec3.xCoord;
					this.motionY = vec3.yCoord;
					this.motionZ = vec3.zCoord;
					this.accelerationX = this.motionX * 0.1D;
					this.accelerationY = this.motionY * 0.1D;
					this.accelerationZ = this.motionZ * 0.1D;
				}

				if (par1DamageSource.getEntity() instanceof EntityLivingBase)
				{
					this.caster = (EntityLivingBase)par1DamageSource.getEntity();
				}

				return true;
			}
			else
			{
				return false;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize()
	{
		return 0.0F;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float par1)
	{
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float par1)
	{
		return 15728880;
	}
}
