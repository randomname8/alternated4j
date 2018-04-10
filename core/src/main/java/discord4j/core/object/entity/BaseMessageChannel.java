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

import discord4j.core.ServiceMediator;
import discord4j.core.object.Snowflake;
import discord4j.core.object.entity.bean.MessageChannelBean;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

/** An internal implementation of {@link MessageChannel} designed to streamline inheritance. */
class BaseMessageChannel extends BaseChannel implements MessageChannel {

    /**
     * Constructs an {@code BaseMessageChannel} with an associated ServiceMediator and Discord data.
     *
     * @param serviceMediator The ServiceMediator associated to this object, must be non-null.
     * @param data The raw data as represented by Discord, must be non-null.
     */
    BaseMessageChannel(final ServiceMediator serviceMediator, final MessageChannelBean data) {
        super(serviceMediator, data);
    }

    @Override
    public Optional<Snowflake> getLastMessageId() {
        return Optional.ofNullable(getData().getLastMessageId()).map(Snowflake::of);
    }

    @Override
    public Mono<Message> getLastMessage() {
        return Mono.justOrEmpty(getLastMessageId()).flatMap(id -> getClient().getMessageById(getId(), id));
    }

    @Override
    public Optional<Instant> getLastPinTimestamp() {
        return Optional.ofNullable(getData().getLastPinTimestamp()).map(Instant::parse);
    }

    @Override
    protected MessageChannelBean getData() {
        return (MessageChannelBean) super.getData();
    }
}
