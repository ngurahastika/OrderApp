package com.example.orderapp.repository.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.orderapp.model.order.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Page<Product> findAll(Pageable pageable);

	@Query("FROM Product p WHERE p.id IN (:ids) ")
	List<Product> getAllProductById(List<Long> ids);

	@Modifying
	@Query("""
			UPDATE Product p
			SET p.stock = p.stock - :qty
			WHERE p.id = :productId
			AND p.stock >= :qty
			""")
	int deductStock(@Param("productId") Long productId, @Param("qty") Integer qty);
}
