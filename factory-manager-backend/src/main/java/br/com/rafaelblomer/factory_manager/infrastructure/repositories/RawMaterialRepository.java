package br.com.rafaelblomer.factory_manager.infrastructure.repositories;

import br.com.rafaelblomer.factory_manager.infrastructure.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {


    boolean existsByCode(String code);
}
