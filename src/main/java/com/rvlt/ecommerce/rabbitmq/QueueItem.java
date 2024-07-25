package com.rvlt.ecommerce.rabbitmq;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueItem {
  private String type;
  private String data;

  @Override
  public String toString() {
    return "QueueItem{" +
            "type='" + type + '\'' +
            ", data='" + data + '\'' +
            '}';
  }
}
