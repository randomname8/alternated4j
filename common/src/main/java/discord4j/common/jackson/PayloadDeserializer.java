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
package discord4j.common.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import discord4j.common.json.payload.GatewayPayload;
import discord4j.common.json.payload.Opcode;
import discord4j.common.json.payload.PayloadData;
import discord4j.common.json.payload.dispatch.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PayloadDeserializer extends StdDeserializer<GatewayPayload<?>> {

    private static final String OP_FIELD = "op";
    private static final String D_FIELD = "d";
    private static final String T_FIELD = "t";
    private static final String S_FIELD = "s";

    public PayloadDeserializer() {
        super(GatewayPayload.class);
    }

    @Override
    public GatewayPayload<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode payload = p.getCodec().readTree(p);

        int op = payload.get(OP_FIELD).asInt();
        String t = payload.get(T_FIELD).asText();
        Integer s = payload.get(S_FIELD).isNull() ? null : payload.get(S_FIELD).intValue();

        Class<? extends PayloadData> payloadType = getPayloadType(op, t);
        PayloadData data = payloadType == null ? null : p.getCodec().treeToValue(payload.get(D_FIELD), payloadType);

        return new GatewayPayload(Opcode.forRaw(op), data, s, t);
    }

    @Nullable
    private static Class<? extends PayloadData> getPayloadType(int op, String t) {
        if (op == Opcode.DISPATCH.getRawOp()) {
            DispatchEvent<?> eventType = DispatchEvent.forName(t);
            if (eventType == null) {
                throw new IllegalArgumentException("Attempt to deserialize payload with unknown event type: " + t);
            }
            return eventType.getDispatchType();
        }

        Opcode<?> opcode = Opcode.forRaw(op);
        if (opcode == null) {
            throw new IllegalArgumentException("Attempt to deserialize payload with unknown op: " + op);
        }
        return opcode.getPayloadType();
    }
}
