package tech.build.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.demo.entity.WalletType;

public interface WalletTypeRepository extends JpaRepository<WalletType, Long> {
}
