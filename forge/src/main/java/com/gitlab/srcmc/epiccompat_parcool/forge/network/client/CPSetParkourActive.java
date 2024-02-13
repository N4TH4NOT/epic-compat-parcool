/*
 * This file is part of Epic Compat: ParCool.
 * Copyright (c) 2024, HDainester, All rights reserved.
 *
 * Epic Compat: ParCool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Epic Compat: ParCool is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Epic Compat: ParCool. If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.gitlab.srcmc.epiccompat_parcool.forge.network.client;

import java.util.function.Supplier;

import com.gitlab.srcmc.epiccompat_parcool.forge.client.capabilities.IParkourPlayerPatch;
import com.gitlab.srcmc.epiccompat_parcool.forge.network.NetworkManager;
import com.gitlab.srcmc.epiccompat_parcool.forge.network.server.SPSetParkourActive;

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