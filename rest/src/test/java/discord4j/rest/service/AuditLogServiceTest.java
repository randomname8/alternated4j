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
package discord4j.rest.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.common.jackson.PossibleModule;
import discord4j.rest.http.EmptyReaderStrategy;
import discord4j.rest.http.EmptyWriterStrategy;
import discord4j.rest.http.JacksonReaderStrategy;
import discord4j.rest.http.JacksonWriterStrategy;
import discord4j.rest.http.client.SimpleHttpClient;
import discord4j.rest.request.Router;
import discord4j.rest.route.Routes;
import org.junit.Test;

import java.util.Collections;

public class AuditLogServiceTest {

    private static final long guild = Long.parseUnsignedLong(System.getenv("guild"));

    private AuditLogService auditLogService = null;

    private AuditLogService getAuditLogService() {

        if (auditLogService != null) {
            return auditLogService;
        }

        String token = System.getenv("token");
        ObjectMapper mapper = getMapper();

        SimpleHttpClient httpClient = SimpleHttpClient.builder()
                .baseUrl(Routes.BASE_URL)
                .defaultHeader("Authorization", "Bot " + token)
                .defaultHeader("Content-Type", "application/json")
                .readerStrategy(new JacksonReaderStrategy<>(mapper))
                .readerStrategy(new EmptyReaderStrategy())
                .writerStrategy(new JacksonWriterStrategy(mapper))
                .writerStrategy(new EmptyWriterStrategy())
                .build();

        Router router = new Router(httpClient);

        return auditLogService = new AuditLogService(router);
    }

    private ObjectMapper getMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .registerModule(new PossibleModule());
    }

    @Test
    public void testGetAuditLog() {
        getAuditLogService().getAuditLog(guild, Collections.emptyMap()).block();
    }
}
