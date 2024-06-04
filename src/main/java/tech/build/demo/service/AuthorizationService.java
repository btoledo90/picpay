package tech.build.demo.service;

import org.springframework.stereotype.Service;
import tech.build.demo.client.AuthorizationClient;
import tech.build.demo.controller.dto.TransferDto;
import tech.build.demo.entity.Transfer;
import tech.build.demo.exception.PicPayException;

@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isAuthorized(TransferDto transfer) {

        var resp = authorizationClient.isAuthorized();

        if (resp.getStatusCode().isError()) {
            throw new PicPayException();
        }

        return resp.getBody().authorized();

    }
}

