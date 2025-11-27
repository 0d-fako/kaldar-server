package com.kaldar.kaldar.controllers;

import com.kaldar.kaldar.dtos.request.AcceptOrderRequest;
import com.kaldar.kaldar.dtos.request.AcceptOrderResponse;
import com.kaldar.kaldar.dtos.request.CreateOrderRequest;
import com.kaldar.kaldar.dtos.response.ApiResponse;
import com.kaldar.kaldar.dtos.response.CreateOrderResponse;
import com.kaldar.kaldar.dtos.response.UpdateOrderStatusResponse;
import com.kaldar.kaldar.kaldarService.interfaces.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.kaldar.kaldar.contants.StatusResponse.ACCEPT_ORDER_SUCCESS_MESSAGE;
import static com.kaldar.kaldar.contants.StatusResponse.ORDER_CREATED_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest){
        CreateOrderResponse createOrderResponse = orderService.placeOrder(createOrderRequest);
        ApiResponse<CreateOrderResponse> apiResponse = ApiResponse.<CreateOrderResponse>builder()
                .isSuccess(true)
                .status(HttpStatus.CREATED.value())
                .message(ORDER_CREATED_SUCCESS_MESSAGE.getMessage())
                .data(createOrderResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    @PostMapping
    public ResponseEntity<ApiResponse<AcceptOrderResponse>> acceptOrder(@RequestBody AcceptOrderRequest acceptOrderRequest){
        AcceptOrderResponse acceptOrderResponse = orderService.acceptOrder(acceptOrderRequest);
        ApiResponse<AcceptOrderResponse> apiResponse = ApiResponse.<AcceptOrderResponse>builder()
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .message(ACCEPT_ORDER_SUCCESS_MESSAGE.getMessage())
                .data(acceptOrderResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<UpdateOrderStatusResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody com.kaldar.kaldar.dtos.request.UpdateOrderStatusRequest request
    ){
        request.setOrderId(orderId);
        UpdateOrderStatusResponse response = orderService.updateOrderStatus(request);
        ApiResponse<UpdateOrderStatusResponse> api = ApiResponse.<UpdateOrderStatusResponse>builder()
                .isSuccess(true)
                .status(HttpStatus.OK.value())
                .message("Order status updated")
                .data(response)
                .build();
        return ResponseEntity.ok(api);
    }
}
