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
package discord4j.core.object.spec;

import discord4j.common.json.OverwriteEntity;
import discord4j.common.json.request.ChannelModifyRequest;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.Snowflake;
import discord4j.core.object.entity.Category;

import javax.annotation.Nullable;
import java.util.Set;

public class TextChannelEditSpec implements Spec<ChannelModifyRequest> {

    private final ChannelModifyRequest.Builder requestBuilder = ChannelModifyRequest.builder();

    public TextChannelEditSpec setName(String name) {
        requestBuilder.name(name);
        return this;
    }

    public TextChannelEditSpec setPosition(int position) {
        requestBuilder.position(position);
        return this;
    }

    public TextChannelEditSpec setNsfw(boolean nsfw) {
        requestBuilder.nsfw(nsfw);
        return this;
    }

    public TextChannelEditSpec setPermissionOverwrites(Set<PermissionOverwrite> permissionOverwrites) {
        OverwriteEntity[] raw = permissionOverwrites.stream()
                .map(o -> new OverwriteEntity(o.getId().asLong(), o.getType().getValue(), o.getAllowed().getRawValue(),
                        o.getDenied().getRawValue()))
                .toArray(OverwriteEntity[]::new);

        requestBuilder.permissionOverwrites(raw);
        return this;
    }

    public TextChannelEditSpec setParentId(@Nullable Snowflake parentId) {
        requestBuilder.parentId(parentId == null ? null : parentId.asLong());
        return this;
    }

    public TextChannelEditSpec setParent(@Nullable Category parent) {
        return setParentId(parent == null ? null : parent.getId());
    }

    @Override
    public ChannelModifyRequest asRequest() {
        return requestBuilder.build();
    }
}
