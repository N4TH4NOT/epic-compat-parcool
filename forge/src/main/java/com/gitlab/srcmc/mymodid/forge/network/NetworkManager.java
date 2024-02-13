package com.gitlab.srcmc.mymodid.forge.network;

import com.gitlab.srcmc.mymodid.ModCommon;
import com.gitlab.srcmc.mymodid.forge.network.client.CPSetParkourActive;
import com.gitlab.srcmc.mymodid.forge.network.server.SPSetParkourActive;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = ModCommon.MOD_ID, bus = Bus.MOD)
public class NetworkManager {
	private static final String PROTOCOL_VERSION = "1";
    
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(ModCommon.MOD_ID, "network_manager"),
        () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static <MSG> void sendToServer(MSG message) {
		INSTANCE.sendToServer(message);
	}
	
	public static <MSG> void sendToClient(MSG message, PacketTarget packetTarget) {
		INSTANCE.send(packetTarget, message);
	}
	
	public static <MSG> void sendToAll(MSG message) {
		sendToClient(message, PacketDistributor.ALL.noArg());
	}

	public static <MSG> void sendToAllPlayerTrackingThisEntity(MSG message, Entity entity) {
		sendToClient(message, PacketDistributor.TRACKING_ENTITY.with(() -> entity));
	}
	
	public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
		sendToClient(message, PacketDistributor.PLAYER.with(() -> player));
	}
	
	public static <MSG> void sendToAllPlayerTrackingThisEntityWithSelf(MSG message, ServerPlayer entity) {
		sendToClient(message, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
	}
	
	public static <MSG> void sendToAllPlayerTrackingThisChunkWithSelf(MSG message, LevelChunk chunk) {
		sendToClient(message, PacketDistributor.TRACKING_CHUNK.with(() -> chunk));
	}

    @SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
        int id = 0;
        INSTANCE.registerMessage(id++, CPSetParkourActive.class, CPSetParkourActive::toBytes, CPSetParkourActive::fromBytes, CPSetParkourActive::handle);
        INSTANCE.registerMessage(id++, SPSetParkourActive.class, SPSetParkourActive::toBytes, SPSetParkourActive::fromBytes, SPSetParkourActive::handle);
	}
}
