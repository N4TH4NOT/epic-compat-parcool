package com.gitlab.srcmc.mymodid.forge.mixins.epicfight.client.world.capabilities.entitypatch.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.gitlab.srcmc.mymodid.forge.client.capabilities.IParkourPlayerPatch;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

@Mixin(PlayerPatch.class)
public abstract class PlayerPatchMixin<T extends Player> extends LivingEntityPatch<T> implements IParkourPlayerPatch {
    protected boolean parkourActive;

	@Inject(method = "isUnstable", at = @At("HEAD"), cancellable = true)
	public void onIsUnstable(CallbackInfoReturnable<Boolean> ci) {
		if(isParkourActive()) {
			ci.setReturnValue(true);
			ci.cancel();
		}
	}

	public boolean isParkourActive() {
		return this.parkourActive;
	}

	public void setParkourActive(boolean value) {
		this.parkourActive = value;
	}
}
