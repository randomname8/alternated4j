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
import discord4j.core.event.Update;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.Snowflake;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public class TextChannelUpdateEvent extends ChannelEvent {

    private final Update<Boolean> nsfw;
    private final Update<String> name;
    private final Update<Snowflake> category;
    private final Update<Set<PermissionOverwrite>> overwrites;
    private final Update<Integer> position;
    private final Update<String> topic;

    public TextChannelUpdateEvent(DiscordClient client, @Nullable Update<Boolean> nsfw, @Nullable Update<String> name,
                                  @Nullable Update<Snowflake> category,
                                  @Nullable Update<Set<PermissionOverwrite>> overwrites,
                                  @Nullable Update<Integer> position, @Nullable Update<String> topic) {
        super(client);
        this.nsfw = nsfw;
        this.name = name;
        this.category = category;
        this.overwrites = overwrites;
        this.position = position;
        this.topic = topic;
    }

    public Optional<Update<Boolean>> isNsfw() {
        return Optional.ofNullable(nsfw);
    }

    public Optional<Update<String>> getName() {
        return Optional.ofNullable(name);
    }

    public Optional<Update<Snowflake>> getCategory() {
        return Optional.ofNullable(category);
    }

    public Optional<Update<Set<PermissionOverwrite>>> getOverwrites() {
        return Optional.ofNullable(overwrites);
    }

    public Optional<Update<Integer>> getPosition() {
        return Optional.ofNullable(position);
    }

    public Optional<Update<String>> getTopic() {
        return Optional.ofNullable(topic);
    }
}
