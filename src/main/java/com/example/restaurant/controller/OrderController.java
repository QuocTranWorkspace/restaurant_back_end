package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.cart.Cart;
import com.example.restaurant.dto.cart.CartItem;
import com.example.restaurant.model.OrderEntity;
import com.example.restaurant.model.OrderProductEntity;
import com.example.restaurant.model.ProductEntity;
import com.example.restaurant.model.UserEntity;
import com.example.restaurant.service.OrderProductService;
import com.example.restaurant.service.OrderService;
import com.example.restaurant.service.ProductService;
import com.example.restaurant.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * The type Order controller.
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final ProductService productService;
    private final OrderProductService orderProductService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Instantiates a new Order controller.
     *
     * @param orderService        the order service
     * @param userService         the user service
     * @param productService      the product service
     * @param orderProductService the order product service
     */
    public OrderController(OrderService orderService, UserService userService, ProductService productService, OrderProductService orderProductService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
        this.orderProductService = orderProductService;
    }

    /**
     * Handle options response entity.
     *
     * @return the response entity
     */
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
    }

    /**
     * Gets order.
     *
     * @param orderId the order id
     * @return the order
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseDTO> getOrder(@PathVariable("orderId") String orderId) {
        OrderEntity order = orderService.getById(Integer.parseInt(orderId));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", order));
    }

    /**
     * Create order 1 response entity.
     *
     * @param user  the user
     * @param order the order
     * @return the response entity
     * @throws JsonProcessingException the json processing exception
     */
    @PostMapping(value = "/checkout")
    public ResponseEntity<ResponseDTO> createOrder1(@RequestPart("user") String user,
                                                    @RequestPart("order") String order) throws JsonProcessingException {
        UserEntity userEntity = objectMapper.readValue(user, UserEntity.class);
        UserEntity userInDB = userService.getById(userEntity.getId());
        if (userInDB != null) {
            if (userInDB.getPhone() == null && userEntity.getPhone() != null && !userEntity.getPhone().isEmpty()) {
                userInDB.setPhone(userEntity.getPhone());
            }
            if (userInDB.getAddress() == null && userEntity.getAddress() != null && !userEntity.getAddress().isEmpty()) {
                userInDB.setAddress(userEntity.getAddress());
            }
            userService.saveOrUpdate(userInDB);
        }

        List<CartItem> itemList = Arrays.asList(objectMapper.readValue(order, CartItem[].class));

        Cart cart = new Cart();
        cart.setCartItems(itemList);

        BigDecimal tempTotal = BigDecimal.ZERO;

        OrderEntity orderSave = new OrderEntity();
        for (CartItem item: cart.getCartItems()) {
            ProductEntity product = productService.getById(item.getProductId());
            tempTotal = tempTotal.add(product.getSalePrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        userInDB = userService.getById(userEntity.getId());

        orderSave.setCode(String.valueOf(System.currentTimeMillis()));
        orderSave.setTotalPrice(tempTotal);
        orderSave.setDeliveryStatus(1);
        assert userInDB != null;
        orderSave.setCustomerName(userEntity.getFirstName() + " " + userEntity.getLastName());
        orderSave.setCustomerEmail(userEntity.getEmail());
        orderSave.setCustomerPhone(userEntity.getPhone());
        orderSave.setCustomerAddress(userEntity.getAddress());
        orderSave.setUser(userInDB);

        orderService.saveOrUpdate(orderSave);

        OrderEntity orderInDB = orderService.findByCode(orderSave.getCode());

        for (CartItem item: cart.getCartItems()) {
            ProductEntity product = productService.getById(item.getProductId());
            OrderProductEntity orderProduct = new OrderProductEntity();
            orderProduct.setOrder(orderInDB);
            orderProduct.setProduct(product);
            orderProduct.setQuantity(item.getQuantity());
            orderProductService.saveOrUpdate(orderProduct);
        }

        return ResponseEntity.ok(new ResponseDTO(200, "update ok", orderInDB));
    }

    /**
     * Gets order by user.
     *
     * @param userId the user id
     * @return the order by user
     */
    @GetMapping("/orderList/{userId}")
    public ResponseEntity<ResponseDTO> getOrderByUser(@PathVariable("userId") String userId) {
        List<OrderEntity> orderList = orderService.searchOrder(userId);
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", orderList));
    }
}
