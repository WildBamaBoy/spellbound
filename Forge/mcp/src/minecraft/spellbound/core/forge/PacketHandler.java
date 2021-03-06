/**********************************************
 * PacketHandler.java
 * Copyright (c) 2013 Wild Bama Boy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 **********************************************/

package spellbound.core.forge;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.MathHelper;
import spellbound.core.Constants;
import spellbound.core.SpellboundCore;
import spellbound.core.util.Logic;
import spellbound.core.util.Point3D;
import spellbound.spells.AbstractSpellWall;
import spellbound.spells.SpellColdShield;
import spellbound.spells.SpellFireShield;
import spellbound.spells.SpellLightningShield;
import spellbound.spells.SpellShieldOfInvulnerability;
import spellbound.spells.SpellSurgeShield;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		try
		{
			final EntityPlayer entityPlayer = (EntityPlayer)player;

			if (packet.channel.equals("SB_LIGHTNING"))
			{
				handleLightningPacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_CHATMESSAGE"))
			{
				handleChatMessagePacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_FLIGHT"))
			{
				handleFlightPacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_WALLGFX"))
			{
				handleWallGFXPacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_COLDGFX"))
			{
				handleColdGFXPacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_SHIELDGFX"))
			{
				handleShieldGFXPacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_COLORGFX"))
			{
				handleColorGFXPacket(packet, entityPlayer);
			}

			else if (packet.channel.equals("SB_SUMMONGFX"))
			{
				handleSummonGFXPacket(packet, entityPlayer);
			}
		}

		catch (ClassNotFoundException exception)
		{
			exception.printStackTrace();
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
		}
	}

	public static Packet250CustomPayload createLightningPacket(double posX, double posY, double posZ)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_LIGHTNING";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(posX);
			objectOutput.writeObject(posY);
			objectOutput.writeObject(posZ);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleLightningPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(byteInput);
		//---------------------------------------------------------------------------------------

		//Read data
		final double posX = (Double) objectInput.readObject();
		final double posY = (Double) objectInput.readObject();
		final double posZ = (Double) objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		final EntityLightningBolt lightning = new EntityLightningBolt(entityPlayer.worldObj, posX, posY, posZ);
		entityPlayer.worldObj.spawnEntityInWorld(lightning);
	}

	public static Packet250CustomPayload createChatMessagePacket(String message)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_CHATMESSAGE";
			//---------------------------------------------------------------------------------------

			//Read data
			objectOutput.writeObject(message);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleChatMessagePacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(byteInput);
		//---------------------------------------------------------------------------------------

		//Read data
		final String message = (String) objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		entityPlayer.addChatMessage(message);
	}

	public static Packet250CustomPayload createFlightPacket(Boolean doEnable)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_FLIGHT";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(doEnable);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleFlightPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream input = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(input);
		//---------------------------------------------------------------------------------------

		//Read data
		final boolean doEnable = (Boolean) objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		if (doEnable)
		{
			if (entityPlayer.onGround)
			{
				entityPlayer.motionY += 1.0D;
			}
			
			entityPlayer.fallDistance = 0;
			entityPlayer.capabilities.allowFlying = true;
			entityPlayer.capabilities.isFlying = true;
		}

		else
		{
			if (!entityPlayer.capabilities.isCreativeMode)
			{
				entityPlayer.capabilities.allowFlying = false;
				entityPlayer.capabilities.isFlying = false;
			}
		}
	}

	public static Packet250CustomPayload createWallGFXPacket(int heading, double centerX, double centerY, double centerZ, boolean isOverhead, boolean isUnderneath)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_WALLGFX";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(heading);
			objectOutput.writeObject(centerX);
			objectOutput.writeObject(centerY);
			objectOutput.writeObject(centerZ);
			objectOutput.writeObject(isOverhead);
			objectOutput.writeObject(isUnderneath);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleWallGFXPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream input = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(input);
		//---------------------------------------------------------------------------------------

		//Read data
		final int heading = (Integer)objectInput.readObject();
		final double centerX = (Double)objectInput.readObject();
		final double centerY = (Double)objectInput.readObject();
		final double centerZ = (Double)objectInput.readObject();
		final boolean isOverhead = (Boolean)objectInput.readObject();
		final boolean isUnderneath = (Boolean)objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		if (isOverhead)
		{
			for (int currentWidth = -3; currentWidth < 3; currentWidth++)
			{
				for (int currentHeight = -3; currentHeight < 3; currentHeight++)
				{
					AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY + 4, centerZ + currentHeight);
					AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY + 2, centerZ + currentHeight);
				}
			}
		}

		else if (isUnderneath)
		{
			for (int currentWidth = -3; currentWidth < 3; currentWidth++)
			{
				for (int currentHeight = -3; currentHeight < 3; currentHeight++)
				{
					AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY, centerZ + currentHeight);
					AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY - 2, centerZ + currentHeight);
				}
			}
		}

		else
		{
			for (int currentWidth = -3; currentWidth < 4; currentWidth++)
			{
				for (int currentHeight = 0; currentHeight < 3; currentHeight++)
				{
					switch (heading)
					{
					case 0:
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY + currentHeight, centerZ + 1);
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY + currentHeight, centerZ - 1);
						break;
					case 1: 
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + 1, centerY + currentHeight, centerZ + currentWidth);
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX - 1, centerY + currentHeight, centerZ + currentWidth);
						break;
					case 2: 
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY + currentHeight, centerZ + 1);
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + currentWidth, centerY + currentHeight, centerZ - 1);
						break;
					case 3: 
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX + 1, centerY + currentHeight, centerZ - currentWidth);
						AbstractSpellWall.addParticles(entityPlayer.worldObj, centerX - 1, centerY + currentHeight, centerZ - currentWidth);
						break;
					default: break;
					}
				}
			}
		}
	}

	public static Packet250CustomPayload createColdGFXPacket(int spellLevel, int heading, double casterX, double casterY, double casterZ)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_COLDGFX";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(spellLevel);
			objectOutput.writeObject(heading);
			objectOutput.writeObject(casterX);
			objectOutput.writeObject(casterY);
			objectOutput.writeObject(casterZ);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleColdGFXPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream input = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(input);
		//---------------------------------------------------------------------------------------

		//Read data
		final int spellLevel = (Integer)objectInput.readObject();
		final int heading = (Integer)objectInput.readObject();
		final double pointX = (Double)objectInput.readObject();
		final double pointY = (Double)objectInput.readObject();
		final double pointZ = (Double)objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		if (spellLevel == 1)
		{
			final boolean addX = heading == 2 || heading == 3;
			final boolean addZ = heading == 0 || heading == 3;

			Integer dummy = 0;
			Integer length = 0;

			for (length = 3; length < 14; length++)
			{
				final int flooredX = MathHelper.floor_double(pointX);
				final int flooredZ = MathHelper.floor_double(pointZ);

				final int xCounter = heading == 0 || heading == 2 ? dummy : length;
				final int zCounter = heading == 0 || heading == 2 ? length : dummy;

				final int posX = addX ? flooredX + xCounter : flooredX - xCounter;
				final int posY = (int) pointY;
				final int posZ = addZ ? flooredZ + zCounter : flooredZ - zCounter;

				final int blockId = entityPlayer.worldObj.getBlockId(posX, posY, posZ);

				if (blockId == Block.snow.blockID || blockId == 0 || blockId == Block.fire.blockID)
				{
					final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
					final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
					final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

					for (int i = 0; i < 6; i++)
					{
						entityPlayer.worldObj.spawnParticle("snowballpoof", posX + SpellboundCore.modRandom.nextFloat(), posY + SpellboundCore.modRandom.nextFloat(), posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
						entityPlayer.worldObj.spawnParticle("snowballpoof", posX + SpellboundCore.modRandom.nextFloat(), posY + 1 + SpellboundCore.modRandom.nextFloat(), posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
					}
				}
			}
		}

		else if (spellLevel == 2)
		{
			for (final Point3D point : Logic.getNearbyBlocks(entityPlayer.worldObj, (int)pointX, (int)pointY, (int)pointZ, 3, Constants.SNOW_SUPPORTERS))
			{
				final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
				final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
				final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

				for (int i = 0; i < 6; i++)
				{
					entityPlayer.worldObj.spawnParticle("snowballpoof", point.posX + SpellboundCore.modRandom.nextFloat(), point.posY + 1 + SpellboundCore.modRandom.nextFloat(), point.posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
					entityPlayer.worldObj.spawnParticle("snowballpoof", point.posX + SpellboundCore.modRandom.nextFloat(), point.posY + 2 + SpellboundCore.modRandom.nextFloat(), point.posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
				}
			}
		}

		else if (spellLevel == 3)
		{
			final boolean addX = heading == 2 || heading == 3;
			final boolean addZ = heading == 0 || heading == 3;

			Integer width = 0;
			Integer length = 0;

			for (width = -3; width < 6; width++)
			{
				for (length = 3; length < 14; length++)
				{
					final int flooredX = MathHelper.floor_double(pointX);
					final int flooredZ = MathHelper.floor_double(pointZ);

					final int xCounter = heading == 0 || heading == 2 ? width : length;
					final int zCounter = heading == 0 || heading == 2 ? length : width;

					final int posX = addX ? flooredX + xCounter : flooredX - xCounter;
					final int posY = (int) pointY;
					final int posZ = addZ ? flooredZ + zCounter : flooredZ - zCounter;

					final int blockId = entityPlayer.worldObj.getBlockId(posX, posY, posZ);

					if (blockId == Block.snow.blockID || blockId == 0 || blockId == Block.fire.blockID)
					{
						final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
						final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
						final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

						for (int i = 0; i < 6; i++)
						{
							entityPlayer.worldObj.spawnParticle("snowballpoof", posX + SpellboundCore.modRandom.nextFloat(), posY + SpellboundCore.modRandom.nextFloat(), posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
							entityPlayer.worldObj.spawnParticle("snowballpoof", posX + SpellboundCore.modRandom.nextFloat(), posY + 1 + SpellboundCore.modRandom.nextFloat(), posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
						}
					}
				}
			}
		}
	}

	public static Packet250CustomPayload createShieldGFXPacket(Class shieldClass, double casterX, double casterY, double casterZ)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_SHIELDGFX";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(shieldClass.getSimpleName());
			objectOutput.writeObject(casterX);
			objectOutput.writeObject(casterY);
			objectOutput.writeObject(casterZ);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleShieldGFXPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream input = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(input);
		//---------------------------------------------------------------------------------------

		//Read data
		final String shieldClass = (String)objectInput.readObject();
		final double casterX = (Double)objectInput.readObject();
		final double casterY = (Double)objectInput.readObject();
		final double casterZ = (Double)objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		if (shieldClass.equals(SpellShieldOfInvulnerability.class.getSimpleName()))
		{
			final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
			final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
			final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

			for (int i = 0; i < 6; i++)
			{
				entityPlayer.worldObj.spawnParticle("flame", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle("snowballpoof", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle("smoke", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle("portal", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);

				entityPlayer.worldObj.spawnParticle("flame", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + 1 + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle("snowballpoof", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + 1 + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle("smoke", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + 1 + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle("portal", casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + 1 + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
			}

			return;
		}

		else
		{
			String particleName = "";

			if (shieldClass.equals(SpellFireShield.class.getSimpleName()))
			{
				particleName = "flame";
			}

			else if (shieldClass.equals(SpellColdShield.class.getSimpleName()))
			{
				particleName = "snowballpoof";
			}

			else if (shieldClass.equals(SpellLightningShield.class.getSimpleName()))
			{
				particleName = "smoke";
			}

			else if (shieldClass.equals(SpellSurgeShield.class.getSimpleName()))
			{
				particleName = "portal";
			}

			final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
			final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
			final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

			for (int i = 0; i < 12; i++)
			{
				entityPlayer.worldObj.spawnParticle(particleName, casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
				entityPlayer.worldObj.spawnParticle(particleName, casterX + SpellboundCore.modRandom.nextFloat() / 2, casterY + 1 + SpellboundCore.modRandom.nextFloat(), casterZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
			}
		}
	}

	public static Packet250CustomPayload createColorGFXPacket(int heading, double casterX, double casterY, double casterZ)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_COLORGFX";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(heading);
			objectOutput.writeObject(casterX);
			objectOutput.writeObject(casterY);
			objectOutput.writeObject(casterZ);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleColorGFXPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream input = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(input);
		//---------------------------------------------------------------------------------------

		//Read data
		final int heading = (Integer)objectInput.readObject();
		final double casterX = (Double)objectInput.readObject();
		final double casterY = (Double)objectInput.readObject();
		final double casterZ = (Double)objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		final boolean addX = heading == 2 || heading == 3;
		final boolean addZ = heading == 0 || heading == 3;

		Integer width = 0;
		Integer length = 0;

		for (width = -3; width < 6; width++)
		{
			for (length = 3; length < 14; length++)
			{
				final int flooredX = MathHelper.floor_double(casterX);
				final int flooredZ = MathHelper.floor_double(casterZ);

				final int xCounter = heading == 0 || heading == 2 ? width : length;
				final int zCounter = heading == 0 || heading == 2 ? length : width;

				final int posX = addX ? flooredX + xCounter : flooredX - xCounter;
				final int posY = (int) casterY;
				final int posZ = addZ ? flooredZ + zCounter : flooredZ - zCounter;

				final String particleName = Constants.SPRAY_PARTICLES[SpellboundCore.modRandom.nextInt(Constants.SPRAY_PARTICLES.length)];

				for (int i = 0; i < 12; i++)
				{
					final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
					final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
					final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

					entityPlayer.worldObj.spawnParticle(particleName, posX + SpellboundCore.modRandom.nextFloat(), posY + SpellboundCore.modRandom.nextFloat(), posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
					entityPlayer.worldObj.spawnParticle(particleName, posX + SpellboundCore.modRandom.nextFloat(), posY + 1 + SpellboundCore.modRandom.nextFloat(), posZ + SpellboundCore.modRandom.nextFloat(), velX, velY, velZ);
				}
			}
		}
	}

	public static Packet250CustomPayload createSummonGFXPacket(double spawnX, double spawnY, double spawnZ)
	{
		try
		{
			//Initialize
			final ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
			final ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
			final Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "SB_SUMMONGFX";
			//---------------------------------------------------------------------------------------

			//Write data
			objectOutput.writeObject(spawnX);
			objectOutput.writeObject(spawnY);
			objectOutput.writeObject(spawnZ);

			//---------------------------------------------------------------------------------------

			//Cleanup and return
			packet.data = byteOutput.toByteArray();
			packet.length = packet.data.length;
			return packet;
		}

		catch (IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}

	private void handleSummonGFXPacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) throws IOException, ClassNotFoundException
	{
		//Initialize
		final ByteArrayInputStream input = new ByteArrayInputStream(packet.data);
		final ObjectInputStream objectInput = new ObjectInputStream(input);
		//---------------------------------------------------------------------------------------

		//Read data
		final double spawnX = (Double)objectInput.readObject();
		final double spawnY = (Double)objectInput.readObject();
		final double spawnZ = (Double)objectInput.readObject();

		//---------------------------------------------------------------------------------------

		//Process
		for (int i = 0; i < 12; i++)
		{
			final double velX = SpellboundCore.modRandom.nextGaussian() * 0.02D;
			final double velY = SpellboundCore.modRandom.nextGaussian() * 0.02D;
			final double velZ = SpellboundCore.modRandom.nextGaussian() * 0.02D;

			entityPlayer.worldObj.spawnParticle("townaura", spawnX + SpellboundCore.modRandom.nextFloat() / 2, spawnY + SpellboundCore.modRandom.nextFloat(), spawnZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
			entityPlayer.worldObj.spawnParticle("townaura", spawnX + SpellboundCore.modRandom.nextFloat() / 2, spawnY + 1 + SpellboundCore.modRandom.nextFloat(), spawnZ + SpellboundCore.modRandom.nextFloat() / 2, velX, velY, velZ);
		}
	}
}
