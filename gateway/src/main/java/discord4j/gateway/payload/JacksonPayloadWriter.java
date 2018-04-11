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
package discord4j.gateway.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import discord4j.common.json.payload.GatewayPayload;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.Exceptions;

public class JacksonPayloadWriter implements PayloadWriter {

  private final ObjectMapper mapper;

  public JacksonPayloadWriter(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public ByteBuf write(GatewayPayload<?> payload) {
    try {
      JsonNodeFactory nodeFactory = mapper.getNodeFactory();
      ObjectNode res = nodeFactory.objectNode();
      res.put(JacksonPayloadReader.T_FIELD, payload.getType());
      if (payload.getSequence() >= 0) res.put(JacksonPayloadReader.S_FIELD, payload.getSequence()); else res.putNull("s");
      res.put(JacksonPayloadReader.OP_FIELD, payload.getOp().getRawOp());
      res.set(JacksonPayloadReader.D_FIELD, mapper.valueToTree(payload.getData()));
      return Unpooled.wrappedBuffer(mapper.writeValueAsBytes(res));
    } catch (JsonProcessingException e) {
      throw Exceptions.propagate(e);
    }
  }
}
