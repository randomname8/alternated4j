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
package discord4j.gateway;

import discord4j.common.jackson.Possible;
import discord4j.common.json.payload.*;
import discord4j.common.json.payload.dispatch.Dispatch;
import discord4j.common.json.payload.dispatch.Ready;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Registry for operating on gateway {@link discord4j.common.json.payload.PayloadData} objects, handling each lifecycle
 * {@link discord4j.common.json.payload.Opcode}.
 */
public abstract class PayloadHandlers {

    private static final Map<Opcode<?>, PayloadHandler<?>> handlerMap = new HashMap<>();
    private static final Logger log = Loggers.getLogger(PayloadHandlers.class);

    static {
        addHandler(Opcode.DISPATCH, PayloadHandlers::handleDispatch);
        addHandler(Opcode.HEARTBEAT, PayloadHandlers::handleHeartbeat);
        addHandler(Opcode.RECONNECT, PayloadHandlers::handleReconnect);
        addHandler(Opcode.INVALID_SESSION, PayloadHandlers::handleInvalidSession);
        addHandler(Opcode.HELLO, PayloadHandlers::handleHello);
        addHandler(Opcode.HEARTBEAT_ACK, PayloadHandlers::handleHeartbeatAck);
    }

    private static <T extends PayloadData> void addHandler(Opcode<T> op, PayloadHandler<T> handler) {
        handlerMap.put(op, handler);
    }

    /**
     * Process a {@link discord4j.common.json.payload.PayloadData} object together with its context, reacting to it.
     *
     * @param context the PayloadContext used with this PayloadData object
     * @param <T> the PayloadData type
     */
    @SuppressWarnings("unchecked")
    public static <T extends PayloadData> void handle(PayloadContext<T> context) {
        PayloadHandler<T> entry = (PayloadHandler<T>) handlerMap.get(context.getPayload().getOp());
        if (entry != null) {
            entry.handle(context);
        }
    }

    private static void handleDispatch(PayloadContext<Dispatch> context) {
        if (context.getData() instanceof Ready) {
            String newSessionId = ((Ready) context.getData()).getSessionId();
            context.getClient().sessionId().set(newSessionId);
        }
        context.getClient().dispatchSink().next(new GatewayEvent<>(null, context.getData()));
    }

    private static void handleHeartbeat(PayloadContext<Heartbeat> context) {
        log.debug("Received heartbeat");
    }

    private static void handleReconnect(PayloadContext<?> context) {
        context.getHandler().error(new RuntimeException("Reconnecting due to reconnect packet received"));
    }

    private static void handleInvalidSession(PayloadContext<InvalidSession> context) {
        // TODO polish
        if (context.getData().isResumable()) {
            String token = context.getClient().token();
            context.getClient().sender().next(GatewayPayload.resume(
                    new Resume(token, context.getClient().getSessionId(), context.getClient().lastSequence().get())));
        } else {
            context.getHandler().error(new RuntimeException("Reconnecting due to non-resumable session invalidation"));
        }
    }

    private static void handleHello(PayloadContext<Hello> context) {
        Duration interval = Duration.ofMillis(context.getData().getHeartbeatInterval());
        context.getClient().heartbeat().start(interval);

        IdentifyProperties props = new IdentifyProperties(System.getProperty("os.name"), "Discord4J", "Discord4J");
        Identify identify = new Identify(context.getClient().token(), props, false, 250,
                Optional.ofNullable(context.getClient().shard().get()).map(Possible::of).orElse(Possible.absent()),
                Optional.ofNullable(context.getClient().status().get()).map(Possible::of).orElse(Possible.absent()));
        GatewayPayload<Identify> response = GatewayPayload.identify(identify);

        context.getClient().sender().next(response);
    }

    private static void handleHeartbeatAck(PayloadContext<?> context) {
        log.debug("Received heartbeat ack");
    }

}
