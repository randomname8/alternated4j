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
package discord4j.core.event.domain.channel;

import discord4j.core.DiscordClient;
import discord4j.core.object.Snowflake;
import discord4j.core.object.entity.MessageChannel;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class PinsUpdateEvent extends ChannelEvent {

    private final long channelId;
    private final Instant lastPinTimestamp;

    public PinsUpdateEvent(DiscordClient client, long channelId, Instant lastPinTimestamp) {
        super(client);
        this.channelId = channelId;
        this.lastPinTimestamp = lastPinTimestamp;
    }

    public Snowflake getChannelId() {
        return Snowflake.of(channelId);
    }

    public Mono<MessageChannel> getChannel() {
        throw new UnsupportedOperationException("Not yet implemented...");
    }

    public Instant getLastPinTimestamp() {
        return lastPinTimestamp;
    }
}
