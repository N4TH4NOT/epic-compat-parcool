package com.gitlab.srcmc.mymodid.forge;

import com.gitlab.srcmc.mymodid.ModCommon;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ModCommon.MOD_ID, bus = Bus.MOD)
public class ModRegistries {
    public static class Sounds {
        public static final DeferredRegister<SoundEvent> REGISTRY;

        static {
            REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModCommon.MOD_ID);
        }
    }

    public static class Items {
        public static final DeferredRegister<Item> REGISTRY;

        static {
            REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModCommon.MOD_ID);
        }
    }

    public static class Blocks {
        public static final DeferredRegister<Block> REGISTRY;

        static {
            REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, ModCommon.MOD_ID);
        }

        public static class Entities {
            public static final DeferredRegister<BlockEntityType<?>> REGISTRY;

            static {
                REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, ModCommon.MOD_ID);
            }
        }
    }

    public static class Entities {
        public static final DeferredRegister<EntityType<?>> REGISTRY;

        static {
            REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITIES, ModCommon.MOD_ID);
        }
    }

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent event) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Sounds.REGISTRY.register(bus);
        Items.REGISTRY.register(bus);
        Blocks.REGISTRY.register(bus);
        Blocks.Entities.REGISTRY.register(bus);
        Entities.REGISTRY.register(bus);
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event) {
        var registry = event.getRegistry();

        Blocks.REGISTRY.getEntries().stream()
            .filter(RegistryObject::isPresent)
            .map(RegistryObject::get)
            .forEach(block -> {
                var bi = new BlockItem(block, new Item.Properties().tab(ModCreativeTab.get()));
                bi.setRegistryName(block.getRegistryName());
                registry.register(bi);
            });
    }
}
