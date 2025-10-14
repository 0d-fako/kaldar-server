package com.kaldar.kaldar.kaldarService.interfaces;

import com.kaldar.kaldar.dtos.request.AcceptOrderRequest;
import com.kaldar.kaldar.dtos.request.AcceptOrderResponse;
import com.kaldar.kaldar.dtos.request.CreateOrderRequest;
import com.kaldar.kaldar.dtos.response.CreateOrderResponse;
import com.kaldar.kaldar.dtos.response.OrderTotalSummaryResponse;

public interface OrderService {
    AcceptOrderResponse acceptOrder(AcceptOrderRequest acceptOrderRequest);

    CreateOrderResponse placeOrder(CreateOrderRequest createOrderRequest);

//    OrderTotalSummaryResponse previewOrder(CreateOrderRequest createOrderRequest);

}
