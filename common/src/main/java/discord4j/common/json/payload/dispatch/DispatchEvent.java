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
package discord4j.common.json.payload.dispatch;

import javax.annotation.Nullable;

public final class DispatchEvent<T extends Dispatch> {

    public static final DispatchEvent<Ready> READY = newEvent("READY", Ready.class);
    public static final DispatchEvent<Resumed> RESUMED = newEvent("RESUMED", Resumed.class);
    public static final DispatchEvent<ChannelCreate> CHANNEL_CREATE = newEvent("CHANNEL_CREATE", ChannelCreate.class);
    public static final DispatchEvent<ChannelUpdate> CHANNEL_UPDATE = newEvent("CHANNEL_UPDATE", ChannelUpdate.class);
    public static final DispatchEvent<ChannelDelete> CHANNEL_DELETE = newEvent("CHANNEL_DELETE", ChannelDelete.class);
    public static final DispatchEvent<ChannelPinsUpdate> CHANNEL_PINS_UPDATE =
            newEvent("CHANNEL_PINS_UPDATE", ChannelPinsUpdate.class);
    public static final DispatchEvent<GuildCreate> GUILD_CREATE = newEvent("GUILD_CREATE", GuildCreate.class);
    public static final DispatchEvent<GuildUpdate> GUILD_UPDATE = newEvent("GUILD_UPDATE", GuildUpdate.class);
    public static final DispatchEvent<GuildDelete> GUILD_DELETE = newEvent("GUILD_DELETE", GuildDelete.class);
    public static final DispatchEvent<GuildBanAdd> GUILD_BAN_ADD = newEvent("GUILD_BAN_ADD", GuildBanAdd.class);
    public static final DispatchEvent<GuildBanRemove> GUILD_BAN_REMOVE =
            newEvent("GUILD_BAN_REMOVE", GuildBanRemove.class);
    public static final DispatchEvent<GuildEmojisUpdate> GUILD_EMOJIS_UPDATE =
            newEvent("GUILD_EMOJIS_UPDATE", GuildEmojisUpdate.class);
    public static final DispatchEvent<GuildIntegrationsUpdate> GUILD_INTEGRATIONS_UPDATE =
            newEvent("GUILD_INTEGRATIONS_UPDATE", GuildIntegrationsUpdate.class);
    public static final DispatchEvent<GuildMemberAdd> GUILD_MEMBER_ADD =
            newEvent("GUILD_MEMBER_ADD", GuildMemberAdd.class);
    public static final DispatchEvent<GuildMemberRemove> GUILD_MEMBER_REMOVE =
            newEvent("GUILD_MEMBER_REMOVE", GuildMemberRemove.class);
    public static final DispatchEvent<GuildMemberUpdate> GUILD_MEMBER_UPDATE =
            newEvent("GUILD_MEMBER_UPDATE", GuildMemberUpdate.class);
    public static final DispatchEvent<GuildMembersChunk> GUILD_MEMBERS_CHUNK =
            newEvent("GUILD_MEMBERS_CHUNK", GuildMembersChunk.class);
    public static final DispatchEvent<GuildRoleCreate> GUILD_ROLE_CREATE =
            newEvent("GUILD_ROLE_CREATE", GuildRoleCreate.class);
    public static final DispatchEvent<GuildRoleUpdate> GUILD_ROLE_UPDATE =
            newEvent("GUILD_ROLE_UPDATE", GuildRoleUpdate.class);
    public static final DispatchEvent<GuildRoleDelete> GUILD_ROLE_DELETE =
            newEvent("GUILD_ROLE_DELETE", GuildRoleDelete.class);
    public static final DispatchEvent<MessageCreate> MESSAGE_CREATE = newEvent("MESSAGE_CREATE", MessageCreate.class);
    public static final DispatchEvent<MessageUpdate> MESSAGE_UPDATE = newEvent("MESSAGE_UPDATE", MessageUpdate.class);
    public static final DispatchEvent<MessageDelete> MESSAGE_DELETE = newEvent("MESSAGE_DELETE", MessageDelete.class);
    public static final DispatchEvent<MessageDeleteBulk> MESSAGE_DELETE_BULK =
            newEvent("MESSAGE_DELETE_BULK", MessageDeleteBulk.class);
    public static final DispatchEvent<MessageReactionAdd> MESSAGE_REACTION_ADD =
            newEvent("MESSAGE_REACTION_ADD", MessageReactionAdd.class);
    public static final DispatchEvent<MessageReactionRemove> MESSAGE_REACTION_REMOVE =
            newEvent("MESSAGE_REACTION_REMOVE", MessageReactionRemove.class);
    public static final DispatchEvent<MessageReactionRemoveAll> MESSAGE_REACTION_REMOVE_ALL =
            newEvent("MESSAGE_REACTION_REMOVE_ALL", MessageReactionRemoveAll.class);
    public static final DispatchEvent<PresenceUpdate> PRESENCE_UPDATE =
            newEvent("PRESENCE_UPDATE", PresenceUpdate.class);
    public static final DispatchEvent<TypingStart> TYPING_START = newEvent("TYPING_START", TypingStart.class);
    public static final DispatchEvent<UserUpdate> USER_UPDATE = newEvent("USER_UPDATE", UserUpdate.class);
    public static final DispatchEvent<VoiceStateUpdateDispatch> VOICE_STATE_UPDATE =
            newEvent("VOICE_STATE_UPDATE", VoiceStateUpdateDispatch.class);
    public static final DispatchEvent<VoiceServerUpdate> VOICE_SERVER_UPDATE =
            newEvent("VOICE_SERVER_UPDATE", VoiceServerUpdate.class);
    public static final DispatchEvent<WebhooksUpdate> WEBHOOKS_UPDATE =
            newEvent("WEBHOOKS_UPDATE", WebhooksUpdate.class);

    private final String name;
    private final Class<T> dispatchType;

    private DispatchEvent(String name, Class<T> dispatchType) {
        this.name = name;
        this.dispatchType = dispatchType;
    }

    @Nullable
    public static DispatchEvent<?> forName(String name) {
        switch (name) {
            case "READY": return READY;
            case "RESUMED": return RESUMED;
            case "CHANNEL_CREATE": return CHANNEL_CREATE;
            case "CHANNEL_UPDATE": return CHANNEL_UPDATE;
            case "CHANNEL_DELETE": return CHANNEL_DELETE;
            case "CHANNEL_PINS_UPDATE": return CHANNEL_PINS_UPDATE;
            case "GUILD_CREATE": return GUILD_CREATE;
            case "GUILD_UPDATE": return GUILD_UPDATE;
            case "GUILD_DELETE": return GUILD_DELETE;
            case "GUILD_BAN_ADD": return GUILD_BAN_ADD;
            case "GUILD_BAN_REMOVE": return GUILD_BAN_REMOVE;
            case "GUILD_EMOJIS_UPDATE": return GUILD_EMOJIS_UPDATE;
            case "GUILD_INTEGRATIONS_UPDATE": return GUILD_INTEGRATIONS_UPDATE;
            case "GUILD_MEMBER_ADD": return GUILD_MEMBER_ADD;
            case "GUILD_MEMBER_REMOVE": return GUILD_MEMBER_REMOVE;
            case "GUILD_MEMBER_UPDATE": return GUILD_MEMBER_UPDATE;
            case "GUILD_MEMBERS_CHUNK": return GUILD_MEMBERS_CHUNK;
            case "GUILD_ROLE_CREATE": return GUILD_ROLE_CREATE;
            case "GUILD_ROLE_UPDATE": return GUILD_ROLE_UPDATE;
            case "GUILD_ROLE_DELETE": return GUILD_ROLE_DELETE;
            case "MESSAGE_CREATE": return MESSAGE_CREATE;
            case "MESSAGE_UPDATE": return MESSAGE_UPDATE;
            case "MESSAGE_DELETE": return MESSAGE_DELETE;
            case "MESSAGE_DELETE_BULK": return MESSAGE_DELETE_BULK;
            case "MESSAGE_REACTION_ADD": return MESSAGE_REACTION_ADD;
            case "MESSAGE_REACTION_REMOVE": return MESSAGE_REACTION_REMOVE;
            case "MESSAGE_REACTION_REMOVE_ALL": return MESSAGE_REACTION_REMOVE_ALL;
            case "PRESENCE_UPDATE": return PRESENCE_UPDATE;
            case "TYPING_START": return TYPING_START;
            case "USER_UPDATE": return USER_UPDATE;
            case "VOICE_STATE_UPDATE": return VOICE_STATE_UPDATE;
            case "VOICE_SERVER_UPDATE": return VOICE_SERVER_UPDATE;
            case "WEBHOOKS_UPDATE": return WEBHOOKS_UPDATE;
            default: return null;
        }
    }

    private static <T extends Dispatch> DispatchEvent<T> newEvent(String name, Class<T> dispatchType) {
        return new DispatchEvent<>(name, dispatchType);
    }

    public String getName() {
        return name;
    }

    public Class<T> getDispatchType() {
        return dispatchType;
    }
}
