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
package discord4j.core.object.entity;

import discord4j.core.object.Snowflake;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

/** A Discord channel that can utilizes messages. */
public interface MessageChannel extends Channel {

    /**
     * Gets the ID of the last message sent in this channel, if present.
     *
     * @return The ID of the last message sent in this channel, if present.
     */
    Optional<Snowflake> getLastMessageId();

    /**
     * Requests to retrieve the last message sent in this channel, if present.
     *
     * @return A {@link Mono} where, upon successful completion, emits the last {@link Message message} sent in this
     * channel, if present. If an error is received, it is emitted through the {@code Mono}.
     */
    Mono<Message> getLastMessage();

    /**
     * Gets when the last pinned message was pinned, if present.
     *
     * @return When the last pinned message was pinned, if present.
     */
    Optional<Instant> getLastPinTimestamp();
}
