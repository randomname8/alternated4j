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

package discord4j.common.json.payload;

import discord4j.common.Lazy;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

public class GatewayPayload<T extends PayloadData> {

    public static int NO_SEQ = -1;
  
    private Opcode<T> op;
    @Nullable
    private Lazy<T> dataSupplier;
    private int sequence;
    @Nullable
    private String type;

    public GatewayPayload(Opcode<T> op, @Nullable Supplier<T> data, int sequence, @Nullable String type) {
        this.op = op;
        this.dataSupplier = Lazy.apply(data);
        this.sequence = sequence;
        this.type = type;
    }

    public GatewayPayload() {
    }

    public static GatewayPayload<Heartbeat> heartbeat(Heartbeat data) {
        return new GatewayPayload<>(Opcode.HEARTBEAT, () -> data, NO_SEQ, null);
    }

    public static GatewayPayload<Identify> identify(Identify data) {
        return new GatewayPayload<>(Opcode.IDENTIFY, () -> data, NO_SEQ, null);
    }

    public static GatewayPayload<StatusUpdate> statusUpdate(StatusUpdate data) {
        return new GatewayPayload<>(Opcode.STATUS_UPDATE, () -> data, NO_SEQ, null);
    }

    public static GatewayPayload<VoiceStateUpdate> voiceStateUpdate(VoiceStateUpdate data) {
        return new GatewayPayload<>(Opcode.VOICE_STATE_UPDATE, () -> data, NO_SEQ, null);
    }

    public static GatewayPayload<Resume> resume(Resume data) {
        return new GatewayPayload<>(Opcode.RESUME, () -> data, NO_SEQ, null);
    }

    public static GatewayPayload<RequestGuildMembers> requestGuildMembers(RequestGuildMembers data) {
        return new GatewayPayload<>(Opcode.REQUEST_GUILD_MEMBERS, () -> data, NO_SEQ, null);
    }

    public Opcode<T> getOp() {
        return op;
    }

    public T getData() {
      return dataSupplier.value();
    }
    
    @Nullable
    public int getSequence() {
        return sequence;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(op, dataSupplier, sequence, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != GatewayPayload.class) {
            return false;
        }

        GatewayPayload<?> other = (GatewayPayload<?>) obj;

        return this.op == other.op
                && Objects.equals(this.dataSupplier, other.dataSupplier)
                && Objects.equals(this.sequence, other.sequence)
                && Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
        return "GatewayPayload[op=" + op + ", data=" + dataSupplier + ", sequence=" + sequence + ", type=" + type + "]";
    }
}
