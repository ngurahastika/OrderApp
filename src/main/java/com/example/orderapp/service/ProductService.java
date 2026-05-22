package com.example.orderapp.service;

import org.springframework.data.domain.Pageable;

import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.ProductResponse;

public interface ProductService {

	public BaseRes listingProduct(Pageable pageable);
	public BaseRes<ProductResponse> detail(Long id) ;

}
