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