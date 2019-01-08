package cn.jgayb.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jone.wang on 2019/1/8.
 * Description:
 */
@Data
@AllArgsConstructor
public class Event {
    private String eventId;
    private String eventDt;
}