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
package discord4j.gateway.payload;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.common.json.payload.GatewayPayload;
import discord4j.common.json.payload.Opcode;
import discord4j.common.json.payload.PayloadData;
import discord4j.common.json.payload.dispatch.DispatchEvent;
import io.netty.buffer.ByteBuf;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public class JacksonPayloadReader implements PayloadReader {

  private static final Logger log = Loggers.getLogger(JacksonPayloadReader.class);

  public static final String OP_FIELD = "op";
  public static final String D_FIELD = "d";
  public static final String T_FIELD = "t";
  public static final String S_FIELD = "s";

  private final ObjectMapper mapper;
  private final boolean lenient;

  public JacksonPayloadReader(ObjectMapper mapper) {
    this(mapper, true);
  }

  public JacksonPayloadReader(ObjectMapper mapper, boolean lenient) {
    this.mapper = mapper;
    this.lenient = lenient;
  }

  @Override
  public GatewayPayload<?> read(ByteBuf payload) {
    try {
      return deserialize(mapper.getFactory().createParser(payload.array()));
    } catch (IOException | IllegalArgumentException | IllegalStateException e) {
      if (lenient) {
        // if eof input - just ignore
        if (payload.readableBytes() > 0) {
          log.warn("Error while decoding JSON ({} bytes): {}. {}", payload.readableBytes(),
            payload.toString(Charset.forName("UTF-8")), e);
        }
        return new GatewayPayload<>();
      } else {
        throw new RuntimeException(String.format("Error while decoding JSON (%d bytes): %s", payload.readableBytes(),
          payload.toString(Charset.forName("UTF-8"))), e);
      }
    }
  }

  public GatewayPayload<?> deserialize(JsonParser p) throws IOException {
    p.nextToken(); //discard the very first object start
    JsonToken token = p.nextToken(); //advance to first field
    int op = -1;
    String t = null;
    int seq = -1;
    Supplier<? extends PayloadData> data = () -> null;
    iteration:
    while (token != null) {
      switch (token) {
        case FIELD_NAME:
          switch (p.getCurrentName()) {
            case OP_FIELD:
              op = p.nextIntValue(op);
              break;
            case T_FIELD:
              t = p.nextTextValue();
              break;
            case S_FIELD:
              seq = p.nextIntValue(seq);
              break;
            case D_FIELD:
              p.nextToken();
              //if the d field was found, then we must have already read the t field
              if (op == -1) throw new IllegalStateException("found d field without an op field");
              else if (op == Opcode.DISPATCH.getRawOp() && t == null) throw new IllegalStateException(
                  "found d field without a t field stating the type");
              Class<? extends PayloadData> payloadType = getPayloadType(op, t);

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
