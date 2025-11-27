package com.kaldar.kaldar.kaldarService.implimentation;
import com.kaldar.kaldar.contants.OrderStatus;
import com.kaldar.kaldar.domain.entities.*;
import com.kaldar.kaldar.domain.repository.*;
import com.kaldar.kaldar.dtos.request.AcceptOrderRequest;
import com.kaldar.kaldar.dtos.request.AcceptOrderResponse;
import com.kaldar.kaldar.dtos.request.CreateOrderRequest;
import com.kaldar.kaldar.dtos.request.UpdateOrderStatusRequest;
import com.kaldar.kaldar.dtos.response.CreateOrderResponse;
import com.kaldar.kaldar.dtos.response.UpdateOrderStatusResponse;
import com.kaldar.kaldar.exceptions.*;
import com.kaldar.kaldar.kaldarService.interfaces.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import static com.kaldar.kaldar.contants.StatusResponse.*;


@Service
public class DefaultOrderService implements OrderService {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(OrderStatus.class);
    static {
        ALLOWED_TRANSITIONS.put(OrderStatus.PENDING_ACCEPTANCE, Set.of(OrderStatus.ACCEPTED, OrderStatus.REJECTED, OrderStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(OrderStatus.ACCEPTED, Set.of(OrderStatus.SCHEDULED));
        ALLOWED_TRANSITIONS.put(OrderStatus.SCHEDULED, Set.of(OrderStatus.PICKED, OrderStatus.PICKED_UP));
        ALLOWED_TRANSITIONS.put(OrderStatus.PICKED, Set.of(OrderStatus.CLEANING));
        ALLOWED_TRANSITIONS.put(OrderStatus.PICKED_UP, Set.of(OrderStatus.CLEANING));
        ALLOWED_TRANSITIONS.put(OrderStatus.CLEANING, Set.of(OrderStatus.READY, OrderStatus.READY_FOR_DELIVERY));
        ALLOWED_TRANSITIONS.put(OrderStatus.READY, Set.of(OrderStatus.OUT_FOR_DELIVERY));
        ALLOWED_TRANSITIONS.put(OrderStatus.READY_FOR_DELIVERY, Set.of(OrderStatus.OUT_FOR_DELIVERY));
        ALLOWED_TRANSITIONS.put(OrderStatus.OUT_FOR_DELIVERY, Set.of(OrderStatus.DELIVERED, OrderStatus.COMPLETED));
        ALLOWED_TRANSITIONS.put(OrderStatus.DELIVERED, Set.of(OrderStatus.COMPLETED));
        // Terminal states have no outgoing transitions
        ALLOWED_TRANSITIONS.put(OrderStatus.REJECTED, Set.of());
        ALLOWED_TRANSITIONS.put(OrderStatus.CANCELLED, Set.of());
        ALLOWED_TRANSITIONS.put(OrderStatus.COMPLETED, Set.of());
        ALLOWED_TRANSITIONS.put(OrderStatus.CREATED, Set.of(OrderStatus.PENDING_ACCEPTANCE));
    }


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

    @Transactional
    @Override
    public UpdateOrderStatusResponse updateOrderStatus(UpdateOrderStatusRequest request) {
        OrderEntity order = orderEntityRepository.findById(request.getOrderId())
                .orElseThrow(() -> new OrdersNotFoundException(ORDERS_NOT_FOUND_EXCEPTION_MESSAGE.getMessage()));

        OrderStatus from = order.getOrderStatus();
        OrderStatus to = request.getStatus();

        if (from == null) {
            // initialize if missing
            from = OrderStatus.CREATED;
        }
        if (!isTransitionAllowed(from, to)) {
            throw new InvalidOrderAssignmentException("Transition not allowed: " + from + " -> " + to);
        }

        order.setOrderStatus(to);
        order.setUpdatedAt(LocalDateTime.now());
        orderEntityRepository.save(order);

        UpdateOrderStatusResponse resp = new UpdateOrderStatusResponse();
        resp.setOrderId(order.getId());
        resp.setStatus(order.getOrderStatus());
        resp.setUpdatedAt(LocalDateTime.now());
        return resp;
    }

    private boolean isTransitionAllowed(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> next = ALLOWED_TRANSITIONS.getOrDefault(from, Collections.emptySet());
        return next.contains(to);
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
