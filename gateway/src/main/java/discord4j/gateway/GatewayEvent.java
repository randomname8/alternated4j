package discord4j.gateway;

import discord4j.common.Lazy;
import discord4j.common.json.payload.dispatch.Dispatch;
import discord4j.common.json.payload.dispatch.DispatchEvent;

public class GatewayEvent<T extends Dispatch> {
  
  public final DispatchEvent<T> eventType;
  public final Lazy<T> data;

  public GatewayEvent(DispatchEvent<T> eventType, Lazy<T> data) {
    this.eventType = eventType;
    this.data = data;
  }
  public GatewayEvent(DispatchEvent<T> eventType, T data) {
    this.eventType = eventType;
    this.data = Lazy.evaluated(data);
  }

  @Override
  public String toString() {
    return "GatewayEvent{" + "eventType=" + eventType + ", data=" + data + '}';
  }
}
