package tech.build.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.build.demo.controller.dto.TransferDto;
import tech.build.demo.entity.Transfer;
import tech.build.demo.entity.Wallet;
import tech.build.demo.exception.InsufficientBalanceException;
import tech.build.demo.exception.TransferNotAllowedForWalletTypeException;
import tech.build.demo.exception.TransferNotAuthorizationException;
import tech.build.demo.exception.WalletNotFoundException;
import tech.build.demo.repository.TransferRepository;
import tech.build.demo.repository.WalletRepository;

import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository,
                           AuthorizationService authorizationService,
                           NotificationService notificationService,
                           WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.walletRepository = walletRepository;
    }
    @Transactional
    public Transfer transfer(TransferDto transferDto) {

        var sender = walletRepository.findById(transferDto.payer())
                .orElseThrow(() -> new WalletNotFoundException(transferDto.payer()));

        var receiver = walletRepository.findById(transferDto.payee())
                .orElseThrow(() -> new WalletNotFoundException(transferDto.payee()));

        validateTransfer(transferDto, sender);

        sender.debit(transferDto.value());
        receiver.credit(transferDto.value());

        var transfer = new Transfer(sender, receiver, transferDto.value());

        walletRepository.save(sender);
        walletRepository.save(receiver);
        var transferResult = transferRepository.save(transfer);

        CompletableFuture.runAsync(() -> notificationService.sendNotification(transferResult));

        return transferResult;
    }

    private void validateTransfer(TransferDto transferDto, Wallet sender) {

        if (!sender.isTransferAllowedForWalletType()){
            throw new TransferNotAllowedForWalletTypeException();
        }

        if (!sender.isBalancerEqualOrGreatherThan(transferDto.value())){
            throw new InsufficientBalanceException();

        }

        if (!authorizationService.isAuthorized(transferDto)) {
            throw new TransferNotAuthorizationException();

        }
    }
}

