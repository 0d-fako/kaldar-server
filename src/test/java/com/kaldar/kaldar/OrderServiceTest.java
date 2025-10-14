package com.kaldar.kaldar;

import com.kaldar.kaldar.contants.ClothType;
import com.kaldar.kaldar.contants.OrderStatus;
import com.kaldar.kaldar.dtos.request.AcceptOrderRequest;
import com.kaldar.kaldar.dtos.request.AcceptOrderResponse;
import com.kaldar.kaldar.dtos.request.CreateOrderRequest;
import com.kaldar.kaldar.dtos.response.CreateOrderResponse;
import com.kaldar.kaldar.dtos.response.OrderTotalSummaryResponse;
import com.kaldar.kaldar.kaldarService.interfaces.OrderService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

import static com.kaldar.kaldar.contants.OrderStatus.ACCEPTED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;


    @Test
    public void testThat_CanCreatOrder(){
        CreateOrderRequest createOrderRequest = buildCreateOrderRequest();
        CreateOrderResponse createOrderResponse = orderService.placeOrder(createOrderRequest);
        assertThat(createOrderResponse).isNotNull();
    }

    private static @NotNull CreateOrderRequest buildCreateOrderRequest() {
        Long customerId = 1L;
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCustomerId(customerId);
        createOrderRequest.setPickupAddress("Lekki phase 1, Lagos, Nigeria");
        createOrderRequest.setDeliveryAddress("Ikate, Lagos, Nigeria");
        createOrderRequest.setWashingPreference("For the shirt please dont use detergent");
        createOrderRequest.setCreatedAt(LocalDateTime.now());
        return createOrderRequest;
    }

    @Test
    public void testThatDryCleanerCanAcceptOrders(){
        Long orderId = 1L;
        Long dryCleanerId = 1L;
        AcceptOrderRequest acceptOrderRequest = new AcceptOrderRequest();
        acceptOrderRequest.setOrderId(orderId);
        acceptOrderRequest.setDryCleanerId(dryCleanerId);
        acceptOrderRequest.setPickupAt(LocalDateTime.now());
        acceptOrderRequest.setOrderStatus(ACCEPTED.toString());
        AcceptOrderResponse acceptOrderResponse = orderService.acceptOrder(acceptOrderRequest);
        assertThat(acceptOrderResponse).isNotNull();
    }



}
