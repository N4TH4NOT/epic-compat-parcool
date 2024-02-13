package com.gitlab.srcmc.mymodid.forge.mixins.epicfight.client.world.capabilities.entitypatch.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.gitlab.srcmc.mymodid.forge.client.capabilities.IParkourPlayerPatch;

import net.minecraft.client.player.AbstractClientPlayer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(AbstractClientPlayerPatch.class)
public abstract class AbstractClientPlayerPatchMixin<T extends AbstractClientPlayer> extends PlayerPatch<T> {
	@Inject(method = "overrideRender", at = @At("HEAD"), cancellable = true)
	public void onOverrideRender(CallbackInfoReturnable<Boolean> ci) {
		var parkourPlayer = (IParkourPlayerPatch)this;

		if(parkourPlayer.isParkourActive()) {
			ci.setReturnValue(false);
			ci.cancel();
		}
	}
}
