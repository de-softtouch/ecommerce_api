package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.enumm.OrderStatus;
import com.api.ecommerceweb.model.*;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.reponse.OrderResponse;
import com.api.ecommerceweb.request.OrderItemRequest;
import com.api.ecommerceweb.request.OrderRequest;
import com.api.ecommerceweb.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderHelper {

    private final OrderService orderService;
    private final ProductModelService productModel;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final PaymentService paymentService;
    private final AddressService addressService;
    private final UserService userService;
    private final PaymentMethodService paymentMethodService;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> order(OrderRequest orderRequest) {
        Order order = new Order();

        Long paymentId = orderRequest.getPaymentId();
        Long addressId = orderRequest.getAddressId();
        Set<OrderItemRequest> orderItems = orderRequest.getItems();
        if (orderItems.isEmpty()) {
            return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Failed to create order: item is empty"));
        }
//        Payment payment = null;
//        if (paymentId == null || (payment = paymentService.getById(paymentId)) == null) {
//            return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Payment method required"));
//        }

        //address
        Address address = null;
        if (addressId == null || (address = addressService.getById(addressId)) == null) {
            return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Shipping address required"));
        }
        order.setAddress(address);
        order.setUser(userService.getCurrentUser());
        order.setStatus(OrderStatus.TO_PAY);
        //set payment
        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethodService.getPaymentMethod(1L));
        payment.setIsSuccess(1);
        payment = paymentService.saveFlush(payment);
        order.setPayment(payment);
        //save
        order = orderService.saveFlush(order);
        for (OrderItemRequest item :
                orderItems) {
            OrderItem orderItem = new OrderItem();
            //check product exist
            Long productId = item.getProductId();
            if (productId == null) {
                return ResponseEntity.badRequest().body(BaseResponseBody.badRequest("Can not find product with id: " + productId));
            }
            Product product = productService.getProduct(productId);
            orderItem.setProduct(product);
            orderItem.setStatus(OrderStatus.TO_PAY);
            orderItem.setQty(item.getQty());
            orderItem.setMessage(item.getMessage());
            //get model
            if (item.getModelId() != null) {
                ProductModel model = this.productModel.getById(item.getModelId());
                orderItem.setModel(model);
            }
            orderItem.setOrder(order);
            orderItemService.save(orderItem);
        }
        return ResponseEntity.ok(BaseResponseBody.success(null, "save order success", "success"));
    }

    public ResponseEntity<?> getCurrentUserOrders(Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        String status = param.get("status");
        List<Order> orderList = orderService.getUserOrders(currentUser.getId(), (ValidationHelper.isNumeric(status)) ? Integer.valueOf(status) : null);
        List<OrderResponse> data = orderList.stream()
                .map(o -> modelMapper.map(o, OrderResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseBody.success(data, "get orders data success", "success"));
    }


    public ResponseEntity<?> getCurrentUserShopOrderItems(Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        String status = param.get("status");
        List<Order> orderList = orderService.getShopOrders(currentUser.getId(), ValidationHelper.isNumeric(status) ? Integer.parseInt(status) : null);

        List<OrderResponse> data = orderList.stream()
                .map(o -> modelMapper.map(o, OrderResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseBody.success(data, "get shop orders data success", "success"));

    }

    public ResponseEntity<?> getOrderItemStatusTypes() {
        User currentUser = userService.getCurrentUser();
        List<Map<String, Object>> list = orderItemService.countOrderItemStatusTypes(currentUser.getId());
        List<OrderItemStatusReport> data = list.stream().map(d -> {
            ObjectMapper mapper = new ObjectMapper();
            OrderItemStatusReport r = mapper.convertValue(d, OrderItemStatusReport.class);
            return r;
        }).collect(Collectors.toList());


        return ResponseEntity.ok(BaseResponseBody.success(data, "get order item status types success", "success"));
    }


}
