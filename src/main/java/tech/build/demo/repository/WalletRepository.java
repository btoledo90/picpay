package tech.build.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.demo.entity.Wallet;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByCpfCnpjOrEmail(String cpfCnpj, String email);
}
