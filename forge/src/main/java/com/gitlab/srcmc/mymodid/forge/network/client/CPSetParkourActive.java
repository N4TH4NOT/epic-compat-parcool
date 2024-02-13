package com.gitlab.srcmc.mymodid.forge.network.client;

import java.util.function.Supplier;

import com.gitlab.srcmc.mymodid.forge.client.capabilities.IParkourPlayerPatch;
import com.gitlab.srcmc.mymodid.forge.network.NetworkManager;
import com.gitlab.srcmc.mymodid.forge.network.server.SPSetParkourActive;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class CPSetParkourActive {
	private final boolean parkourActive;

	public CPSetParkourActive() {
		this.parkourActive = false;
	}

	public CPSetParkourActive(boolean parkourActive) {
		this.parkourActive = parkourActive;
	}

	public static CPSetParkourActive fromBytes(FriendlyByteBuf buf) {
		return new CPSetParkourActive(buf.readBoolean());
	}

	public static void toBytes(CPSetParkourActive msg, FriendlyByteBuf buf) {
		buf.writeBoolean(msg.parkourActive);
	}
	
	public static void handle(CPSetParkourActive msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			
			if (player != null) {
				var entitypatch = (IParkourPlayerPatch)EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
				
				if (entitypatch != null) {
                    entitypatch.setParkourActive(msg.parkourActive);
					NetworkManager.sendToAllPlayerTrackingThisEntity(new SPSetParkourActive(msg.parkourActive, player.getId()), player);
				}
			}
		});

		ctx.get().setPacketHandled(true);
	}
}