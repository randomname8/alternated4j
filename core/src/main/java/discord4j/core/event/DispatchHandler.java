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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J. If not, see <http://www.gnu.org/licenses/>.
 */

package discord4j.core.event;

import discord4j.common.json.payload.dispatch.Dispatch;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Flux;

/**
 * Handler for the gateway Dispatch events.
 *
 * @param <D> the inbound Dispatch type
 * @param <E> the outbound Event type
 */
@FunctionalInterface
public interface DispatchHandler<D extends Dispatch, E extends Event> {

    /**
     * Operates and transforms a Dispatch event with its context, from gateway to user-friendly Events, so it may be
     * further routed to an event dispatcher downstream.
     * <p>
     * The context allows access to underlying client resources for operations like caching.
     *
     * @param context the dispatch context
     * @return a Flux of Events that are derived from the given dispatch context
     */
    Flux<E> handle(DispatchContext<D> context);
}
