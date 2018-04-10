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
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import discord4j.common.json.payload.GatewayPayload;
import discord4j.common.json.payload.Opcode;
import discord4j.common.json.payload.PayloadData;
import discord4j.common.json.payload.dispatch.*;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

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

    JsonToken token = p.nextToken();
    int op = -1;
    String t = null;
    int seq = -1;
    Supplier<? extends PayloadData> data = () -> {
      throw new UnsupportedOperationException("This gateway event doesn't have data");
    };
    iteration:
    while (token != null) {
      switch (token) {
        case FIELD_NAME:
          switch (p.getCurrentName()) {
            case OP_FIELD:
              p.nextToken();
              op = p.getIntValue();
              break;
            case T_FIELD:
              p.nextToken();
              t = p.getText();
              break;
            case S_FIELD:
              p.nextToken();
              seq = p.getIntValue();
              break;
            case D_FIELD:
              p.nextToken();
              //if the d field was found, then we must have already read the t field
              if (t == null) throw new IllegalStateException("found d field without a t field stating the type");
              Class<? extends PayloadData> payloadType = getPayloadType(0, t);
              data = () -> {
                try {
                  return p.readValueAs(payloadType);
                } catch (IOException ex) {
                  throwAsUnchecked(ex);
                  return null;
                }
              };
              break iteration; //when the d field is found, it is the last field in the payload //when the d field is found, it is the last field in the payload
            default:
          }
          token = p.nextToken();
          break;
        case START_OBJECT:
        case START_ARRAY:
          p.skipChildren();
          continue;
        case END_OBJECT:
          break iteration;
        default:
          token = p.nextToken();
      }
    }

    return new GatewayPayload(Opcode.forRaw(op), data, seq, t);
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

  @SuppressWarnings("unchecked")
  private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
    throw (E) exception;
  }
}
