package discord4j.gateway;

import discord4j.common.Lazy;
import discord4j.common.json.payload.dispatch.Dispatch;

public class GatewayEvent<T extends Dispatch> {
  
  public final String eventName;
  public final Lazy<T> data;

  public GatewayEvent(String eventName, Lazy<T> data) {
    this.eventName = eventName;
    this.data = data;
  }
  public GatewayEvent(String eventName, T data) {
    this.eventName = eventName;
    this.data = Lazy.evaluated(data);
  }

  @Override
  public String toString() {
    return "GatewayEvent{" + "eventName=" + eventName + ", data=" + data + '}';
  }
}
