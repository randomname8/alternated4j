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
package discord4j.core.object.entity.bean;

import discord4j.common.json.response.ChannelResponse;

import java.util.Objects;

public final class VoiceChannelBean extends GuildChannelBean {

    private static final long serialVersionUID = 8207820651946724351L;

    private int bitrate;
    private int userLimit;

    public VoiceChannelBean(final ChannelResponse response) {
        super(response);
        bitrate = Objects.requireNonNull(response.getBitrate());
        userLimit = Objects.requireNonNull(response.getUserLimit());
    }

    public VoiceChannelBean() {}

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(final int bitrate) {
        this.bitrate = bitrate;
    }

    public int getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(final int userLimit) {
        this.userLimit = userLimit;
    }
}
