package br.com.rafaelblomer.factory_manager.infrastructure.repositories;

import br.com.rafaelblomer.factory_manager.infrastructure.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByCode(String code);
}
