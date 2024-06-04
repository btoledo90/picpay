package tech.build.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.build.demo.entity.Transfer;

@FeignClient(
        name = "NotificationClient",
        url = "${client.notification-service.url}"
)
public interface NotificationClient {

    @PostMapping
    ResponseEntity<Void> sendNotofication(@RequestBody Transfer transfer);
}