package tech.build.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.demo.entity.Transfer;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
