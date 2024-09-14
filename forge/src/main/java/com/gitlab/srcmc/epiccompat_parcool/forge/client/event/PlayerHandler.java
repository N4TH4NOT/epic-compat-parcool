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
package com.gitlab.srcmc.epiccompat_parcool.forge.client.event;

import com.alrex.parcool.common.action.impl.FastRun;
import com.alrex.parcool.common.capability.Parkourability;
import com.gitlab.srcmc.epiccompat_parcool.ModCommon;
import com.gitlab.srcmc.epiccompat_parcool.forge.client.capabilities.IParkourPlayerPatch;
import com.gitlab.srcmc.epiccompat_parcool.forge.network.NetworkManager;
import com.gitlab.srcmc.epiccompat_parcool.forge.network.client.CPSetParkourActive;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import yesman.epicfight.client.ClientEngine;

@Mod.EventBusSubscriber(modid = ModCommon.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class PlayerHandler {
    @SubscribeEvent
    public static void onTickLocal(PlayerTickEvent event) {
        if(event.player.isLocalPlayer()) {
            var parkourPlayer = (IParkourPlayerPatch)ClientEngine.getInstance().getPlayerPatch();

            if(parkourPlayer != null) {
                var parkourAbility = Parkourability.get(event.player);

                if(parkourAbility != null) {
                    for(var action : parkourAbility.getList()) {
                        if(action instanceof FastRun) {
                            continue; // use epic fight/vanilla animations
                        }

                        if(action.isDoing()) {
                            if(!parkourPlayer.isParkourActive()) {
                                parkourPlayer.setParkourActive(true);
                                NetworkManager.sendToServer(new CPSetParkourActive(true));
                            }

                            return;
                        }
                    }
                }
                
                if(parkourPlayer.isParkourActive()) {
                    parkourPlayer.setParkourActive(false);
                    NetworkManager.sendToServer(new CPSetParkourActive(false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockUse(PlayerInteractEvent.RightClickBlock event) {
        var player = (IParkourPlayerPatch)ClientEngine.getInstance().getPlayerPatch();
        if (player == null) return;

        if (player.isParkourActive()) event.setCanceled(true);
    }
}