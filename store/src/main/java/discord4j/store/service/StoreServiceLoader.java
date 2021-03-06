/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.store.service;

import discord4j.store.Store;
import discord4j.store.noop.NoOpStoreService;
import discord4j.store.primitive.ForwardingStoreService;
import discord4j.store.primitive.LongObjStore;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A factory-esque object which provides save objects from {@link StoreService}s.
 *
 * @see StoreService
 */
public class StoreServiceLoader {

    private final List<StoreService> services = new LinkedList<>();

    private final StoreService generalService;

    /**
     * Creates a reusable instance of the provider, service discovery occurs at this point!
     */
    public StoreServiceLoader() {
        ServiceLoader<StoreService> serviceLoader = ServiceLoader.load(StoreService.class);

        serviceLoader.iterator().forEachRemaining(services::add);

        services.add(new NoOpStoreService()); //No-op is lowest priority

        StoreService generic = getGenericStoreProvider();
        StoreService primitive = getLongObjStoreProvider();
        generalService = generic == primitive ? generic : new ComposedStoreService(generic, primitive);
    }

    /**
     * Gets the {@link StoreService} implementation to use.
     *
     * @return The best {@link StoreService} implementation.
     */
    public StoreService getStoreService() {
        return generalService;
    }

    /**
     * Gets the service which will be used to provide generic stores.
     *
     * @return The generic store providing service.
     */
    StoreService getGenericStoreProvider() {
        return services.stream().filter(StoreService::hasGenericStores).findFirst().get();
    }

    /**
     * Gets the service which will be used to provide long-object stores.
     *
     * @return The long-object store providing service.
     */
    StoreService getLongObjStoreProvider() {
        StoreService service = services.stream().filter(StoreService::hasLongObjStores).findFirst().get();

        if (service instanceof NoOpStoreService) { //Fallback to boxed impl if one is present
            StoreService backup = getGenericStoreProvider();
            if (!(backup instanceof NoOpStoreService)) {
                return new ForwardingStoreService(backup);
            }
        }

        return service;
    }

    /**
     * Generates a new generic store instance from the most appropriate service.
     *
     * @param keyClass The class of the keys.
     * @param valueClass The class of the values.
     * @param <K> The key type which provides a 1:1 mapping to the value type. This type is also expected to be
     * {@link Comparable} in order to allow for range operations.
     * @param <V> The value type.
     * @return A mono which provides a store instance.
     */
    public <K extends Comparable<K>, V extends Serializable> Store<K, V> newGenericStore(Class<K> keyClass,
                                                                                         Class<V> valueClass) {
        return getStoreService().provideGenericStore(keyClass, valueClass);
    }

    /**
     * Generates a new long-object store instance from the most appropriate service.
     *
     * @param valueClass The class of the values.
     * @param <V> The value type.
     * @return A mono which provides a store instance.
     */
    public <V extends Serializable> LongObjStore<V> newLongObjStore(Class<V> valueClass) {
        return getStoreService().provideLongObjStore(valueClass);
    }

    private static final class ComposedStoreService implements StoreService {

        private final StoreService genericService, primitiveService;

        private ComposedStoreService(StoreService genericService, StoreService primitiveService) {
            this.genericService = genericService;
            this.primitiveService = primitiveService;
        }

        @Override
        public boolean hasGenericStores() {
            return genericService.hasGenericStores();
        }

        @Override
        public <K extends Comparable<K>, V extends Serializable> Store<K, V> provideGenericStore(Class<K> keyClass,
                                                                                                 Class<V> valueClass) {
            return genericService.provideGenericStore(keyClass, valueClass);
        }

        @Override
        public boolean hasLongObjStores() {
            return primitiveService.hasLongObjStores();
        }

        @Override
        public <V extends Serializable> LongObjStore<V> provideLongObjStore(Class<V> valueClass) {
            return primitiveService.provideLongObjStore(valueClass);
        }
    }
}
