/*
 * Copyright (C) 2017-2022 Illusive Soulworks
 * Derived from comforts mod:
 * https://github.com/illusivesoulworks/comforts
 *
 * Simple Text Overlay is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Simple Text Overlay is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Simple Text Overlay.  If not, see <https://www.gnu.org/licenses/>.
 */

package simpletextoverlay.registry;

import java.util.Collection;
import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import simpletextoverlay.platform.Services;

public interface RegistryProvider<T> {

    static <T> RegistryProvider<T> get(ResourceKey<? extends Registry<T>> resourceKey, String modId, boolean makeRegistry) {
        return Services.REGISTRY_FACTORY.create(resourceKey, modId, makeRegistry);
    }

    static <T> RegistryProvider<T> get(ResourceKey<? extends Registry<T>> resourceKey, String modId) {
        return get(resourceKey, modId, false);
    }

    static <T> RegistryProvider<T> get(Registry<T> registry, String modId, boolean makeRegistry) {
        return Services.REGISTRY_FACTORY.create(registry, modId, makeRegistry);
    }

    static <T> RegistryProvider<T> get(Registry<T> registry, String modId) {
        return get(registry, modId, false);
    }

    <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier);

    Collection<RegistryObject<T>> getEntries();

    String getModId();

}
