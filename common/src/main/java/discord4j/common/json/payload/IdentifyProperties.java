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

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdentifyProperties {

    @JsonProperty("$os")
    private final String os;
    @JsonProperty("$browser")
    private final String browser;
    @JsonProperty("$device")
    private final String device;

    public IdentifyProperties(String os, String browser, String device) {
        this.os = os;
        this.browser = browser;
        this.device = device;
    }

    @Override
    public String toString() {
        return "IdentifyProperties[" +
                "os=" + os +
                ", browser=" + browser +
                ", device=" + device +
                ']';
    }
}
