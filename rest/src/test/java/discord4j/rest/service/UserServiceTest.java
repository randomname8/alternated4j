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
import discord4j.common.jackson.Possible;
import discord4j.common.jackson.PossibleModule;
import discord4j.common.json.request.DMCreateRequest;
import discord4j.common.json.request.UserModifyRequest;
import discord4j.rest.http.EmptyReaderStrategy;
import discord4j.rest.http.EmptyWriterStrategy;
import discord4j.rest.http.JacksonReaderStrategy;
import discord4j.rest.http.JacksonWriterStrategy;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.http.client.SimpleHttpClient;
import discord4j.rest.request.Router;
import discord4j.rest.route.Routes;
import org.junit.Test;

public class UserServiceTest {

    private static final long user = Long.parseUnsignedLong(System.getenv("member"));

    private UserService userService = null;

    private UserService getUserService() {

        if (userService != null) {
            return userService;
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

        return userService = new UserService(router);
    }

    private ObjectMapper getMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .registerModule(new PossibleModule());
    }

    @Test
    public void testGetCurrentUser() {
        getUserService().getCurrentUser().block();
    }

    @Test
    public void testGetUser() {
        getUserService().getUser(user).block();
    }

    @Test
    public void testGetInvalidUser() {
        try {
            getUserService().getUser(1111222).block(); // should throw ClientException
        } catch (ClientException e) {
            e.getErrorResponse().blockOptional().ifPresent(response -> {
                System.out.println("Error code: " + response.getCode());
                System.out.println("Error message: " + response.getMessage());
            });
        }
    }

    @Test
    public void testModifyCurrentUser() {
        UserModifyRequest req = new UserModifyRequest(Possible.of("Discord4J 3 Test Bot"), Possible.absent());
        getUserService().modifyCurrentUser(req).block();
    }

    @Test
    public void testGetCurrentUserGuilds() {
        getUserService().getCurrentUserGuilds().then().block();
    }

    @Test
    public void testLeaveGuild() {
        // TODO
    }

    @Test
    public void testGetUserDMs() {
        getUserService().getUserDMs().then().block();
    }

    @Test
    public void testCreateDM() {
        DMCreateRequest req = new DMCreateRequest(user);
        getUserService().createDM(req).block();
    }

    @Test
    public void testCreateGroupDM() {
        // TODO
    }

    @Test
    public void testGetUserConnections() {
        // TODO
    }

}
