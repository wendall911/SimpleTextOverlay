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

package simpletextoverlay.platform;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import simpletextoverlay.overlay.compass.PinInfoRegistry;
import simpletextoverlay.registry.RegistryObject;
import simpletextoverlay.registry.RegistryProvider;
import simpletextoverlay.platform.services.IRegistryFactory;

public class FabricRegistryProvider implements IRegistryFactory {

    @Override
    public <T> RegistryProvider<T> create(ResourceKey<? extends Registry<T>> resourceKey,
            String modId, boolean makeRegistry) {
        return new Provider<>(modId, resourceKey, makeRegistry);
    }

    @Override
    public <T> RegistryProvider<T> create(Registry<T> registry, String modId, boolean makeRegistry) {
        return new Provider<>(modId, registry, makeRegistry);
    }

    private static class Provider<T> implements RegistryProvider<T> {
        private final String modId;
        private final Registry<T> registry;

        private final Set<RegistryObject<T>> entries = new HashSet<>();
        private final Set<RegistryObject<T>> entriesView = Collections.unmodifiableSet(entries);

        @SuppressWarnings({"unchecked"})
        private Provider(String modId, ResourceKey<? extends Registry<T>> key, boolean makeRegistry) {
            this.modId = modId;

            if (makeRegistry) {
                var reg = FabricRegistryBuilder.createSimple(PinInfoRegistry.class, key.location()).buildAndRegister();

                registry = (Registry<T>) reg;
            }
            else {
                var reg = Registry.REGISTRY.get(key.location());

                if (reg == null) {
                    throw new RuntimeException("Registry with name " + key.location() + " was not found!");
                }

                registry = (Registry<T>) reg;
            }
        }

        private Provider(String modId, Registry<T> registry, boolean makeRegistry) {
            this.modId = modId;
            this.registry = registry;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <I extends T> RegistryObject<I> register(String name, Supplier<? extends I> supplier) {
            final var rl = new ResourceLocation(modId, name);
            final var obj = Registry.register(registry, rl, supplier.get());
            final var ro = new RegistryObject<I>() {
                final ResourceKey<I> key =
                    ResourceKey.create((ResourceKey<? extends Registry<I>>) registry.key(), rl);

                @Override
                public ResourceKey<I> getResourceKey() {
                    return key;
                }

                @Override
                public ResourceLocation getId() {
                    return rl;
                }

                @Override
                public I get() {
                    return obj;
                }

                @Override
                public Holder<I> asHolder() {
                    return (Holder<I>) registry.getOrCreateHolder((ResourceKey<T>) this.key);
                }
            };
            entries.add((RegistryObject<T>) ro);
            return ro;
        }

        @Override
        public Collection<RegistryObject<T>> getEntries() {
            return entriesView;
        }

        @Override
        public String getModId() {
            return modId;
        }
    }

}
