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
package discord4j.core.util;

import discord4j.common.json.response.ChannelResponse;
import discord4j.core.ServiceMediator;
import discord4j.core.object.entity.*;
import discord4j.core.object.entity.bean.*;

/** An utility class for entity processing. */
public final class EntityUtil {

    /**
     * An utility that converts some instance of {@code ChannelResponse} to its associated {@code ChannelBean}
     * {@link Channel.Type type}. That is to say, {@code response.getType() == ChannelBean#getType()}.
     *
     * @param response The {@code ChannelResponse} to convert.
     * @return The converted {@code ChannelBean}.
     */
    public static ChannelBean getChannelBean(final ChannelResponse response) {
        switch (Channel.Type.of(response.getType())) {
            case GUILD_TEXT: return new TextChannelBean(response);
            case DM: return new PrivateChannelBean(response);
            case GUILD_VOICE: return new VoiceChannelBean(response);
            case GUILD_CATEGORY: return new CategoryBean(response);
            default: throw new UnsupportedOperationException("Unknown Response: " + response);
        }
    }

    /**
     * An utility that converts some instance of {@code ChannelBean} to its associated {@code Channel}
     * {@link Channel.Type type}. That is to say, {@code bean.getType() == Channel#getType().getValue()}.
     *
     * @param bean The {@code ChannelBean} to convert.
     * @return The converted {@code Channel}.
     */
    public static Channel getChannel(final ServiceMediator serviceMediator, final ChannelBean bean) {
        switch (Channel.Type.of(bean.getType())) {
            case GUILD_TEXT: return new TextChannel(serviceMediator, (TextChannelBean) bean);
            case DM: return new PrivateChannel(serviceMediator, (PrivateChannelBean) bean);
            case GUILD_VOICE: return new VoiceChannel(serviceMediator, (VoiceChannelBean) bean);
            case GUILD_CATEGORY: return new Category(serviceMediator, (CategoryBean) bean);
            default: throw new UnsupportedOperationException("Unknown Bean: " + bean);
        }
    }

    private EntityUtil() {}
}
