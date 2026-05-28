package com.example.orderapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.orderapp.constants.CommonConstant;
import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.Pagination;
import com.example.orderapp.dto.ProductResponse;
import com.example.orderapp.exception.DataNotFoundException;
import com.example.orderapp.model.order.Product;
import com.example.orderapp.repository.order.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	public BaseRes<List<ProductResponse>> listingProduct(Pageable pageable) {
		BaseRes<List<ProductResponse>> response = new BaseRes<List<ProductResponse>>();
		try {

			Page<Product> page = this.productRepository.findAll(pageable);

			List<ProductResponse> listProduct = page.getContent().stream().map(p -> {
				ProductResponse pr = new ProductResponse();
				pr.setId(p.getId());
				pr.setName(p.getName());
				pr.setPrice(p.getPrice());
				pr.setStock(p.getStock());
				return pr;
			}).toList();

			Pagination pagination = new Pagination();
			pagination.setMaxRow(page.getSize());
			pagination.setPage(page.getNumber());
			pagination.setTotalPage(page.getTotalPages());
			pagination.setTotalRecord(page.getNumberOfElements());

			response.setData(listProduct);
			response.setPagination(pagination);
			response.setStatusCode(CommonConstant.STATUS_CODE_SUCCESS);
			response.setStatusDesc(CommonConstant.STATUS_DESC_SUCCESS);
		} catch (Exception e) {
			log.error("error :", e);
			throw e;
		}
		return response;
	}

	public BaseRes<ProductResponse> detail(Long id) {
		BaseRes<ProductResponse> response = new BaseRes<ProductResponse>();
		try {

			ProductResponse pr = this.productRepository.findById(id)
					.map(product -> ProductResponse.builder().id(product.getId()).name(product.getName())
							.stock(product.getStock()).price(product.getPrice()).build())
					.orElseThrow(() -> new DataNotFoundException("Product Not Found"));

			response.setData(pr);
			response.setStatusCode(CommonConstant.STATUS_CODE_SUCCESS);
			response.setStatusDesc(CommonConstant.STATUS_DESC_SUCCESS);
		} catch (Exception e) {
			log.error("error :", e);
			throw e;
		}
		return response;
	}

}
