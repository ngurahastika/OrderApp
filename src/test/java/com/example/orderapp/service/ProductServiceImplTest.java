package com.example.orderapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.Pagination;
import com.example.orderapp.dto.ProductResponse;
import com.example.orderapp.exception.DataNotFoundException;
import com.example.orderapp.model.order.Product;
import com.example.orderapp.repository.order.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductServiceImpl productService;

	@Test
	void listingProductSuccess() {

		Product product1 = new Product();
		product1.setId(1L);
		product1.setName("Laptop");
		product1.setPrice(BigDecimal.valueOf(10000000));
		product1.setStock(10);

		Product product2 = new Product();
		product2.setId(2L);
		product2.setName("Mouse");
		product2.setPrice(BigDecimal.valueOf(150000));
		product2.setStock(20);

		List<Product> products = List.of(product1, product2);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Product> page = new PageImpl<>(products, pageable, 2);

		when(productRepository.findAll(pageable)).thenReturn(page);

		BaseRes<List<ProductResponse>> response = productService.listingProduct(pageable);

		assertEquals("00", response.getStatusCode());

		assertNotNull(response.getData());

		assertEquals(2, response.getData().size());

		assertEquals("Laptop", response.getData().get(0).getName());

		assertEquals(BigDecimal.valueOf(10000000), response.getData().get(0).getPrice());

		assertEquals(10, response.getData().get(0).getStock());

		Pagination pagination = response.getPagination();

		assertEquals(0, pagination.getPage());

		assertEquals(10, pagination.getMaxRow());

		assertEquals(1, pagination.getTotalPage());

		assertEquals(2, pagination.getTotalRecord());

		verify(productRepository).findAll(pageable);
	}

	@Test
	void listingProductEmptySuccess() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<Product> page = new PageImpl<>(List.of(), pageable, 0);

		when(productRepository.findAll(pageable)).thenReturn(page);

		BaseRes<List<ProductResponse>> response = productService.listingProduct(pageable);

		assertEquals("00", response.getStatusCode());

		

		assertEquals(0, response.getData().size());
	}

	@Test
	void detailSuccess() {

		Product product = new Product();

		product.setId(1L);
		product.setName("Laptop");
		product.setPrice(BigDecimal.valueOf(10000000));
		product.setStock(10);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		BaseRes<ProductResponse> response = productService.detail(1L);

		assertEquals("00", response.getStatusCode());

		assertNotNull(response.getData());

		ProductResponse data = response.getData();

		assertEquals(1L, data.getId());

		assertEquals("Laptop", data.getName());

		assertEquals(BigDecimal.valueOf(10000000), data.getPrice());

		assertEquals(10, data.getStock());

		verify(productRepository).findById(1L);
	}

	@Test
	void detailNotFound() {

		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(DataNotFoundException.class, () -> productService.detail(1L));

		verify(productRepository).findById(1L);
	}
}
