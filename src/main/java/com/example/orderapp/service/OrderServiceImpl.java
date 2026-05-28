package com.example.orderapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.constants.CommonConstant;
import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.CreateOrderRequest;
import com.example.orderapp.dto.CreatedOrderResponse;
import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.dto.OrderItemRequest;
import com.example.orderapp.dto.ProsesOrderDto;
import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.exception.BadRequestException;
import com.example.orderapp.exception.DataNotFoundException;
import com.example.orderapp.model.order.Order;
import com.example.orderapp.model.order.OrderItem;
import com.example.orderapp.model.order.Product;
import com.example.orderapp.repository.order.OrderItemRepository;
import com.example.orderapp.repository.order.OrderRepository;
import com.example.orderapp.repository.order.ProductRepository;
import com.example.orderapp.utils.DateUtils;
import com.example.orderapp.utils.SnowflakeIdGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final OrderProducerService producer;
	private final SnowflakeIdGenerator snowflakeIdGenerator;
	private final DataOrderService dataOrderService;

	public BaseRes<CreatedOrderResponse> createOrder(CreateOrderRequest request) throws Exception {
		BaseRes<CreatedOrderResponse> response = new BaseRes<>();
		try {

			BigDecimal total = BigDecimal.ZERO;

			List<Long> idsProduct = request.getItems().stream().map(x -> x.getProductId()).toList();

			Map<Long, Product> mapProduct = this.productRepository.getAllProductById(idsProduct).stream()
					.collect(Collectors.toMap(Product::getId, Function.identity()));

			Order order = new Order();
			order.setId(snowflakeIdGenerator.nextId());
			order.setCreatedBy(request.getUserId());
			order.setStatus(OrderStatus.PENDING);
			order.setUserId(request.getUserId());

			List<OrderItem> listOrder = new ArrayList<>();

			for (OrderItemRequest item : request.getItems()) {

				Product p = mapProduct.get(item.getProductId());

				if (p == null) {
					throw new BadRequestException("Product Not Found");
				}

				if (item.getQuantity() <= 0) {
					throw new BadRequestException("Quantity invalid");
				}

				if (p.getStock() < item.getQuantity()) {
					throw new BadRequestException("Stock tidak cukup");
				}

				total = total.add(p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

				OrderItem orderItem = new OrderItem();

				orderItem.setId(snowflakeIdGenerator.nextId());
				orderItem.setOrderId(order.getId());
				orderItem.setProductId(p.getId());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setPrice(p.getPrice());

				orderItem.setCreatedBy(request.getUserId());
				orderItem.setCreatedDt(DateUtils.getCurrentSQLTimestamp());

				listOrder.add(orderItem);
			}

			order.setTotalAmount(total);

			dataOrderService.saveOrder(order, listOrder);

			producer.publishCreated(new ProsesOrderDto(request.getUserId(), order.getId()));

			CreatedOrderResponse resp = new CreatedOrderResponse();
			resp.setOrderId(order.getId());

			response.setData(resp);
			response.setStatusCode(CommonConstant.STATUS_CODE_SUCCESS);
			response.setStatusDesc(CommonConstant.STATUS_DESC_SUCCESS);
		} catch (Exception e) {
			log.error("error : ", e);
			throw e;
		}
		return response;
	}

	@Transactional
	public void prosesOrder(ProsesOrderDto order) throws JsonProcessingException, AmqpException {
		boolean isFailed = false;
		try {
			List<OrderItem> items = orderItemRepository.findByOrderId(order.getOrderId());

			if (items.isEmpty()) {
				throw new DataNotFoundException();
			}

			for (OrderItem item : items) {
				if (this.productRepository.deductStock(item.getProductId(), item.getQuantity()) <= 0) {
					throw new BadRequestException();
				}
			}
		} catch (Exception e) {
			isFailed = true;
			log.error("error ", e);
			throw e;
		} finally {
			if (isFailed) {
				this.dataOrderService.updateStatusOrder(order.getOrderId(), OrderStatus.FAILED);
				producer.publishFailed(new NotifDto(order.getOrderId(), order.getUserId(), OrderStatus.FAILED));
			} else {
				this.dataOrderService.updateStatusOrder(order.getOrderId(), OrderStatus.PAID);
				producer.publishFailed(new NotifDto(order.getOrderId(), order.getUserId(), OrderStatus.PAID));
			}
		}
	}

	@Override
	public BaseRes<CreatedOrderResponse> detail(Long id) {
		BaseRes<CreatedOrderResponse> response = new BaseRes<>();
		try {

			CreatedOrderResponse orderRes = this.orderRepository.findById(id)
					.map(order -> new CreatedOrderResponse(order.getId(), order.getStatus(), order.getTotalAmount()))
					.orElseThrow(() -> new DataNotFoundException("Order Not Found"));

			List<OrderItemRequest> items = orderItemRepository.findByOrderId(id).stream().map(itm -> {

				OrderItemRequest item = new OrderItemRequest();

				item.setProductId(itm.getProductId());

				item.setQuantity(itm.getQuantity());

				return item;
			}).toList();

			orderRes.setItems(items);
			response.setData(orderRes);
			response.setStatusCode(CommonConstant.STATUS_CODE_SUCCESS);
			response.setStatusDesc(CommonConstant.STATUS_DESC_SUCCESS);
		} catch (Exception e) {
			log.error("error :", e);
			throw e;
		}

		return response;
	}
}