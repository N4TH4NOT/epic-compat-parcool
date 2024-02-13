package com.gitlab.srcmc.mymodid.forge.network.server;

import java.util.function.Supplier;

import com.gitlab.srcmc.mymodid.forge.client.capabilities.IParkourPlayerPatch;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SPSetParkourActive {
	private final boolean parkourActive;
	private final int entityId;

	public SPSetParkourActive() {
		this.parkourActive = false;
		this.entityId = 0;
	}

	public SPSetParkourActive(boolean parkourActive, int entityId) {
		this.parkourActive = parkourActive;
		this.entityId = entityId;
	}

	public static SPSetParkourActive fromBytes(FriendlyByteBuf buf) {
		return new SPSetParkourActive(buf.readBoolean(), buf.readInt());
	}

	public static void toBytes(SPSetParkourActive msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.parkourActive);
		buf.writeInt(msg.entityId);
	}
	
	public static void handle(SPSetParkourActive msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			Entity entity = mc.level.getEntity(msg.entityId);
			
			if (entity != null) {
				var entitypatch = (IParkourPlayerPatch)EpicFightCapabilities.getEntityPatch(entity, PlayerPatch.class);
				
				if (entitypatch != null) {
                    entitypatch.setParkourActive(msg.parkourActive);
				}
			}
		});

		ctx.get().setPacketHandled(true);
	}
}