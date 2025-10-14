package com.kaldar.kaldar.kaldarService.implimentation;
import com.kaldar.kaldar.contants.OrderStatus;
import com.kaldar.kaldar.domain.entities.*;
import com.kaldar.kaldar.domain.repository.*;
import com.kaldar.kaldar.dtos.request.AcceptOrderRequest;
import com.kaldar.kaldar.dtos.request.AcceptOrderResponse;
import com.kaldar.kaldar.dtos.request.CreateOrderRequest;
import com.kaldar.kaldar.dtos.response.CreateOrderResponse;
import com.kaldar.kaldar.exceptions.*;
import com.kaldar.kaldar.kaldarService.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import static com.kaldar.kaldar.contants.StatusResponse.*;


@Service
public class DefaultOrderService implements OrderService {


    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final CustomerEntityRepository customerEntityRepository;
    private final DryCleanerEntityRepository dryCleanerEntityRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final OrderEntityRepository orderEntityRepository;
    private final OrderServiceItemRepository orderServiceItemRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public DefaultOrderService(CustomerEntityRepository customerEntityRepository,
                               DryCleanerEntityRepository dryCleanerEntityRepository,
                               ServiceOfferingRepository serviceOfferingRepository,
                               OrderEntityRepository orderEntityRepository, OrderServiceItemRepository orderServiceItemRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.customerEntityRepository = customerEntityRepository;
        this.dryCleanerEntityRepository = dryCleanerEntityRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.orderEntityRepository = orderEntityRepository;
        this.orderServiceItemRepository = orderServiceItemRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    @Override
    public AcceptOrderResponse acceptOrder(AcceptOrderRequest acceptOrderRequest) {
        DryCleanerEntity dryCleaner = dryCleanerEntityRepository.findById(acceptOrderRequest.getDryCleanerId())
                .orElseThrow(()-> new UserNotFoundException(DRY_CLEANER_NOT_FOUND_EXCEPTION_MESSAGE.getMessage()));
        OrderEntity order = orderEntityRepository.findById(acceptOrderRequest.getOrderId())
                .orElseThrow(()-> new OrdersNotFoundException(ORDERS_NOT_FOUND_EXCEPTION_MESSAGE.getMessage()));
        if (order.getDryCleaner() == null || !order.getDryCleaner().getId().equals(dryCleaner.getId())){
            throw new InvalidOrderAssignmentException("Order not assigned to this drycleaner");
        }
        if (order.getOrderStatus() != OrderStatus.PENDING_ACCEPTANCE)
            throw new InvalidOrderAssignmentException("Order cannot be accepted");
        if (Boolean.FALSE.equals(dryCleaner.isActive()))
            throw new NoActiveDryCleanerException(dryCleaner.getId());
        List<String> missingService = findMissingService(order,dryCleaner);
        if (!missingService.isEmpty()){
            throw new MissingServicesNotEmptyException("Missing Service" + String.join(" ", missingService));
        }
        if (order.getPickupAt() == null)
            order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setPickupAt(LocalDateTime.now().plusHours(24));
       OrderEntity orderEntity= orderEntityRepository.save(order);
//       applicationEventPublisher.publishEvent(new OrderAcceptedEvent);
        AcceptOrderResponse acceptOrderResponse = new AcceptOrderResponse();
        acceptOrderResponse.setOrderId(order.getId());
        acceptOrderResponse.setStatus("ACCEPTED");
        acceptOrderResponse.setTimestamp(LocalDateTime.now());
        return acceptOrderResponse;
    }

    @Override
    public CreateOrderResponse placeOrder(CreateOrderRequest createOrderRequest) {
        return null;
    }

    private List<String> findMissingService(OrderEntity order, DryCleanerEntity dryCleaner) {
        return null;
    }

//    @Transactional
//    @Override
//    public CreateOrderResponse placeOrder(CreateOrderRequest createOrderRequest) {
//        CustomerEntity customer = customerEntityRepository.findById(createOrderRequest.getCustomerId())
//                .orElseThrow(()-> new UserNotFoundException(CUSTOMER_NOT_FOUND_EXCEPTION_MESSAGE.getMessage()));
//        DryCleanerEntity dryCleaner = dryCleanerEntityRepository.findById(createOrderRequest.getDryCleanerId())
//                .orElseThrow(()-> new UserNotFoundException(DRY_CLEANER_NOT_FOUND_EXCEPTION_MESSAGE.getMessage()));
//        OrderEntity order = new OrderEntity();
//        order.setCustomer(customer);
//        order.setDryCleaner(dryCleaner);
//        order.setPickupAddress(createOrderRequest.getPickupAddress());
//        order.setDeliveryAddress(createOrderRequest.getDeliveryAddress());
//        order.setWashingPreference(createOrderRequest.getWashingPreference());
//        order.setOrderStatus(OrderStatus.CREATED);
//        order.setCreatedAt(createOrderRequest.getCreatedAt());
//        order = orderEntityRepository.save(order);
//        double total = 0.0;
//        for (OrderServiceItem orderServiceItem : createOrderRequest.getServiceItems()){
//            ServiceOffering serviceOffering = serviceOfferingRepository.findById(orderServiceItem.getServiceOffering().getId())
//                    .orElseThrow(()-> new ServicesNotFoundException("Service not found"));
//            OrderServiceItem serviceItem = new OrderServiceItem();
//            serviceItem.setOrder(order);
//            serviceItem.setServiceOffering(serviceOffering);
//            serviceItem.setQuantity(orderServiceItem.getQuantity());
//            serviceItem.setPriceSnapshot(orderServiceItem.getPriceSnapshot());
//            order.getOrderServiceItems().add(serviceItem);
//            orderServiceItemRepository.save(serviceItem);
//            total += serviceOffering.getUnitPrice() * orderServiceItem.getQuantity();
//        }
//        order.setTotalAmount(total);
//        return mapToCreateOrderResponse(order);
//    }
//
//    private static @NotNull CreateOrderResponse mapToCreateOrderResponse(OrderEntity order) {
//        CreateOrderResponse createOrderResponse = new CreateOrderResponse();
//        createOrderResponse.setCustomerId(order.getCustomer().getId());
//        createOrderResponse.setDryCleanerId(order.getDryCleaner().getId());
//        createOrderResponse.setPickupAddress(order.getPickupAddress());
//        createOrderResponse.setDeliveryAddress(order.getDeliveryAddress());
//        createOrderResponse.setTotalPrice(order.getTotalAmount());
//        createOrderResponse.setCreatedAt(order.getCreatedAt());
//        createOrderResponse.setStatus(ORDER_CREATED_SUCCESS_MESSAGE.getMessage());
//        return createOrderResponse;
//    }


}
