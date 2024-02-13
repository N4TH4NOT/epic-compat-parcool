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
package com.gitlab.srcmc.epiccompat_parcool.forge.mixins.epicfight.client.world.capabilities.entitypatch.player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.gitlab.srcmc.epiccompat_parcool.forge.client.capabilities.IParkourPlayerPatch;
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