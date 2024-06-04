package tech.build.demo.service;

import org.springframework.stereotype.Service;
import tech.build.demo.controller.dto.CreateWalletDto;
import tech.build.demo.entity.Wallet;
import tech.build.demo.exception.WalletDataAlreadyExistsException;
import tech.build.demo.repository.WalletRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {

        var walletDb = walletRepository.findByCpfCnpjOrEmail(dto.cpfCnpj(), dto.email());
        if (walletDb.isPresent()) {
            throw new WalletDataAlreadyExistsException("CpfCnpj or Email already exists");

        }

        return walletRepository.save(dto.toWallet());
    }
}
