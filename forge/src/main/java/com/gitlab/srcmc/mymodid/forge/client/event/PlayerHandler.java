package com.gitlab.srcmc.mymodid.forge.client.event;

import com.alrex.parcool.common.action.impl.FastRun;
import com.alrex.parcool.common.capability.Parkourability;
import com.gitlab.srcmc.mymodid.ModCommon;
import com.gitlab.srcmc.mymodid.forge.client.capabilities.IParkourPlayerPatch;
import com.gitlab.srcmc.mymodid.forge.network.NetworkManager;
import com.gitlab.srcmc.mymodid.forge.network.client.CPSetParkourActive;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
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
            
            for(var action : Parkourability.get(event.player).getList()) {
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
            
            if(parkourPlayer.isParkourActive()) {
                parkourPlayer.setParkourActive(false);
                NetworkManager.sendToServer(new CPSetParkourActive(false));
            }
        }
    }
}
