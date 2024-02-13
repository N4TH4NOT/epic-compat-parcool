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
package com.gitlab.srcmc.epiccompat_parcool.forge.network.server;

import java.util.function.Supplier;

import com.gitlab.srcmc.epiccompat_parcool.forge.client.capabilities.IParkourPlayerPatch;

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