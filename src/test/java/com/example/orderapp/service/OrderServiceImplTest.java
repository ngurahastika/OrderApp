package com.example.orderapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
import com.example.orderapp.utils.SnowflakeIdGenerator;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderItemRepository orderItemRepository;

	@Mock
	private OrderProducerService producer;

	@Mock
	private SnowflakeIdGenerator snowflakeIdGenerator;

	@Mock
	private DataOrderService dataOrderService;

	@InjectMocks
	private OrderServiceImpl orderService;

	@Test
	void createOrderSuccess() throws Exception {

		Product product = new Product();
		product.setId(1L);
		product.setPrice(BigDecimal.valueOf(10000));
		product.setStock(10);

		when(productRepository.getAllProductById(anyList())).thenReturn(List.of(product));

		when(snowflakeIdGenerator.nextId()).thenReturn(1001L).thenReturn(2001L);

		doNothing().when(dataOrderService).saveOrder(any(), anyList());

		CreateOrderRequest request = new CreateOrderRequest();
		request.setUserId("user-001");

		OrderItemRequest item = new OrderItemRequest();
		item.setProductId(1L);
		item.setQuantity(2);

		request.setItems(List.of(item));

		BaseRes<CreatedOrderResponse> response = orderService.createOrder(request);

		assertEquals("00", response.getStatusCode());

		assertNotNull(response.getData());

		assertEquals(1001L, response.getData().getOrderId());

		verify(productRepository).getAllProductById(anyList());

		verify(dataOrderService).saveOrder(any(), anyList());

		verify(producer).publishCreated(any(ProsesOrderDto.class));

		ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

		ArgumentCaptor<List<OrderItem>> itemCaptor = ArgumentCaptor.forClass(List.class);

		verify(dataOrderService).saveOrder(orderCaptor.capture(), itemCaptor.capture());

		Order savedOrder = orderCaptor.getValue();

		assertEquals(1001L, savedOrder.getId());

		assertEquals(OrderStatus.PENDING, savedOrder.getStatus());

		assertEquals(BigDecimal.valueOf(20000), savedOrder.getTotalAmount());

		List<OrderItem> itemsSaved = itemCaptor.getValue();

		assertEquals(1, itemsSaved.size());

		OrderItem savedItem = itemsSaved.get(0);

		assertEquals(2001L, savedItem.getId());

		assertEquals(1L, savedItem.getProductId());

		assertEquals(2, savedItem.getQuantity());
	}

	@Test
	void createOrderProductNotFound() {

		when(productRepository.getAllProductById(anyList())).thenReturn(List.of());

		CreateOrderRequest request = new CreateOrderRequest();

		request.setUserId("user-001");

		OrderItemRequest item = new OrderItemRequest();
		item.setProductId(1L);
		item.setQuantity(1);

		request.setItems(List.of(item));

		assertThrows(BadRequestException.class, () -> orderService.createOrder(request));

		verify(dataOrderService, never()).saveOrder(any(), anyList());
	}

	@Test
	void createOrderInvalidQuantity() {

		Product product = new Product();
		product.setId(1L);
		product.setPrice(BigDecimal.valueOf(10000));
		product.setStock(10);

		when(productRepository.getAllProductById(anyList())).thenReturn(List.of(product));

		CreateOrderRequest request = new CreateOrderRequest();

		request.setUserId("user-001");

		OrderItemRequest item = new OrderItemRequest();
		item.setProductId(1L);
		item.setQuantity(0);

		request.setItems(List.of(item));

		assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
	}

	@Test
	void createOrderStockNotEnough() {

		Product product = new Product();
		product.setId(1L);
		product.setPrice(BigDecimal.valueOf(10000));
		product.setStock(1);

		when(productRepository.getAllProductById(anyList())).thenReturn(List.of(product));

		CreateOrderRequest request = new CreateOrderRequest();

		request.setUserId("user-001");

		OrderItemRequest item = new OrderItemRequest();
		item.setProductId(1L);
		item.setQuantity(5);

		request.setItems(List.of(item));

		assertThrows(BadRequestException.class, () -> orderService.createOrder(request));
	}

	@Test
	void prosesOrderSuccess() throws Exception {

	    OrderItem item = new OrderItem();

	    item.setProductId(1L);
	    item.setQuantity(2);

	    when(orderItemRepository.findByOrderId(1001L))
	            .thenReturn(List.of(item));

	    when(productRepository.deductStock(1L, 2))
	            .thenReturn(1);

	    when(dataOrderService.updateStatusOrder(
	            anyLong(),
	            any(OrderStatus.class)
	    )).thenReturn(1);

	    ProsesOrderDto dto =
	            new ProsesOrderDto("user-001", 1001L);

	    orderService.prosesOrder(dto);

	    verify(productRepository)
	            .deductStock(1L, 2);

	    verify(dataOrderService)
	            .updateStatusOrder(1001L, OrderStatus.PAID);

	    verify(producer)
	            .publishFailed(any(NotifDto.class));
	}
	@Test
	void prosesOrderFailedStock() {

		OrderItem item = new OrderItem();

		item.setProductId(1L);
		item.setQuantity(10);

		when(orderItemRepository.findByOrderId(1001L)).thenReturn(List.of(item));

		when(productRepository.deductStock(1L, 10)).thenReturn(0);

		ProsesOrderDto dto = new ProsesOrderDto("user-001", 1001L);

		assertThrows(BadRequestException.class, () -> orderService.prosesOrder(dto));

		verify(dataOrderService).updateStatusOrder(1001L, OrderStatus.FAILED);
	}

	@Test
	void detailSuccess() {

		Order order = new Order();

		order.setId(1001L);
		order.setStatus(OrderStatus.PAID);
		order.setTotalAmount(BigDecimal.valueOf(50000));

		when(orderRepository.findById(1001L)).thenReturn(Optional.of(order));

		OrderItem item1 = new OrderItem();
		item1.setProductId(1L);
		item1.setQuantity(2);

		OrderItem item2 = new OrderItem();
		item2.setProductId(2L);
		item2.setQuantity(1);

		when(orderItemRepository.findByOrderId(1001L)).thenReturn(List.of(item1, item2));

		BaseRes<CreatedOrderResponse> response = orderService.detail(1001L);

		assertEquals("00", response.getStatusCode());

		assertNotNull(response.getData());

		assertEquals(1001L, response.getData().getOrderId());

		assertEquals(OrderStatus.PAID, response.getData().getStatus());

		assertEquals(BigDecimal.valueOf(50000), response.getData().getTotalAmount());

		assertEquals(2, response.getData().getItems().size());

		assertEquals(1L, response.getData().getItems().get(0).getProductId());
	}

	@Test
	void detailNotFound() {

		when(orderRepository.findById(1001L)).thenReturn(Optional.empty());

		assertThrows(DataNotFoundException.class, () -> orderService.detail(1001L));
	}
}