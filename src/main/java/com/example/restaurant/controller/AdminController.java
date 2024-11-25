package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.dto.user.SignUpDTO;
import com.example.restaurant.dto.user.UserDTO;
import com.example.restaurant.model.*;
import com.example.restaurant.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * The type Admin controller.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final ProductService productService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Instantiates a new Admin controller.
     *
     * @param userService the user service
     */
    public AdminController(UserService userService, CategoryService categoryService, OrderService orderService,
                           OrderProductService orderProductService, ProductService productService,
                           RoleService roleService, UserRoleService userRoleService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.orderProductService = orderProductService;
        this.orderService = orderService;
        this.productService = productService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
    }

    /**
     * Signup admin response dto.
     *
     * @param user the user
     * @return the response dto
     */
    @PostMapping("/register")
    public ResponseDTO signupAdmin(@RequestBody SignUpDTO user) {
        return userService.register(user, "ADMIN");
    }

    //Category actions

    /**
     * Update category response entity.
     *
     * @param id       the id
     * @param category the category
     * @return the response entity
     */
    @PostMapping("/category/{categoryId}")
    public ResponseEntity<ResponseDTO> updateCategory(@PathVariable("categoryId") String id, @RequestBody CategoryEntity category) {
        CategoryEntity categorySave = categoryService.getById(Integer.parseInt(id));
        if (Objects.nonNull(categorySave) && !Objects.isNull(category)) {
            categorySave.setCategoryName(category.getCategoryName());
            categorySave.setCategoryDescription(category.getCategoryDescription());
            categorySave.setUpdatedDate(new Date());
            categoryService.saveOrUpdate(categorySave);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "updateOk", categorySave));
    }

    /**
     * Create category response entity.
     *
     * @param category the category
     * @return the response entity
     */
    @PostMapping("/category/addCategory")
    public ResponseEntity<ResponseDTO> createCategory(@RequestBody CategoryEntity category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        if (Objects.nonNull(category)) {
            categoryEntity.setCategoryName(category.getCategoryName());
            categoryEntity.setCategoryDescription(category.getCategoryDescription());
            categoryService.saveOrUpdate(categoryEntity);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "updateOkay", categoryEntity));
    }

    /**
     * Delete order response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/category/deleteCategory/{categoryId}")
    public ResponseEntity<ResponseDTO> deleteOrder1(@PathVariable("categoryId") String id) {
        CategoryEntity category = categoryService.getById(Integer.parseInt(id));
        category.setStatus(false);
        for (ProductEntity product : category.getProducts()) {
            productService.getById(product.getId()).setStatus(false);
            productService.saveOrUpdate(product);
        }
        category.setProducts(new HashSet<>());
        categoryService.saveOrUpdate(category);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted1", category));
    }

    //Order actions

    /**
     * Gets all order.
     *
     * @return the all order
     */
    @GetMapping("/order/orderList")
    public ResponseEntity<ResponseDTO> getAllOrder() {
        List<OrderEntity> orderList = orderService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "orderList", orderList));
    }

    /**
     * Update order response entity.
     *
     * @param id    the id
     * @param order the order
     * @return the response entity
     */
    @PostMapping("/order/{orderId}")
    public ResponseEntity<ResponseDTO> updateOrder(@PathVariable("orderId") String id, @RequestBody OrderEntity order) {
        OrderEntity orderUpdate = orderService.getById(Integer.parseInt(id));
        OrderEntity orderResponse = null;
        if (Objects.nonNull(orderUpdate) && !Objects.isNull(order)) {
            orderResponse = orderService.bindingOrderData(orderUpdate, order);
            orderUpdate.setUpdatedDate(new Date());
            orderService.saveOrUpdate(orderUpdate);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "updateAuk", orderResponse));
    }

    /**
     * Create order response entity.
     *
     * @param order the order
     * @return the response entity
     */
    @PostMapping("/order/addOrder")
    public ResponseEntity<ResponseDTO> createOrder(@RequestBody OrderEntity order) {
        OrderEntity orderEntity = new OrderEntity();
        OrderEntity orderResponse = null;
        if (Objects.nonNull(order)) {
            UserEntity user = userService.findByEmail(order.getCustomerEmail());
            if (Objects.nonNull(user)) {
                orderResponse = orderService.bindingOrderData(orderEntity, order);
                orderEntity.setUser(user);
                orderService.saveOrUpdate(orderEntity);
            }
        }
        return ResponseEntity.ok(new ResponseDTO(200, "updateK", orderResponse));
    }

    /**
     * Delete order response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/order/deleteOrder/{orderId}")
    public ResponseEntity<ResponseDTO> deleteOrder2(@PathVariable("orderId") String id) {
        OrderEntity order = orderService.getById(Integer.parseInt(id));
        order.setStatus(false);
        orderService.saveOrUpdate(order);
        OrderEntity orderResponse = new OrderEntity();
        orderService.bindingOrderData(orderResponse, order);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted2", orderResponse));
    }

    /**
     * Gets order by code.
     *
     * @param orderCode the order code
     * @return the order by code
     */
    @GetMapping("/order/detail/{orderCode}")
    public ResponseEntity<ResponseDTO> getOrderByCode(@PathVariable("orderCode") String orderCode) {
        List<OrderProductEntity> orderList = orderProductService.searchOrderProducts(orderCode);
        return ResponseEntity.ok(new ResponseDTO(200, "get ok1", orderList));
    }

    // Product actions

    /**
     * Update product response entity.
     *
     * @param id      the id
     * @param avatar  the avatar
     * @param product the product
     * @return the response entity
     * @throws JsonProcessingException the json processing exception
     */
    @PostMapping(value = "/product/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> updateProduct(@PathVariable("productId") String id,
                                                     @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                     @RequestPart("product") String product) throws JsonProcessingException {
        ProductEntity productUpdate = productService.getById(Integer.parseInt(id));
        ProductEntity productGet = objectMapper.readValue(product, ProductEntity.class);
        ProductEntity productResponse = null;
        if (Objects.nonNull(productUpdate) && !Objects.isNull(product)) {
            productResponse = productService.bindingProductData(productUpdate, productGet);
            productUpdate.setUpdatedDate(new Date());
            productService.updateProduct(productUpdate, avatar);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok2", productResponse));
    }

    /**
     * Create product response entity.
     *
     * @param categoryId the category id
     * @param avatar     the avatar
     * @param product    the product
     * @return the response entity
     * @throws JsonProcessingException the json processing exception
     */
    @PostMapping(value = "/product/addProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO> createProduct(@RequestPart(value = "category", required = false) String categoryId,
                                                     @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                                     @RequestPart("product") String product) throws JsonProcessingException {
        ProductEntity productGet = objectMapper.readValue(product, ProductEntity.class);
        CategoryEntity categoryEntity = categoryService.getById(Integer.parseInt(categoryId));
        productGet.setCategory(categoryEntity);

        productService.saveProduct(productGet, avatar);
        return ResponseEntity.ok(new ResponseDTO(200, "update ok3", productGet));
    }

    /**
     * Delete order response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/product/deleteProduct/{productId}")
    public ResponseEntity<ResponseDTO> deleteOrder(@PathVariable("productId") String id) {
        ProductEntity productUpdate = productService.getById(Integer.parseInt(id));
        productUpdate.setStatus(false);
        productService.saveOrUpdate(productUpdate);
        ProductEntity productResponse = new ProductEntity();
        productService.bindingProductData(productUpdate, productResponse);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted4", productResponse));
    }

    // Role actions

    /**
     * Gets role list.
     *
     * @return the role list
     */
    @GetMapping("/role/roleList")
    public ResponseEntity<ResponseDTO> getRoleList() {
        List<RoleEntity> roleList = roleService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok5", roleList));
    }

    /**
     * Gets user.
     *
     * @param roleId the role id
     * @return the user
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<ResponseDTO> getUser(@PathVariable("roleId") String roleId) {
        RoleEntity role = roleService.getById(Integer.parseInt(roleId));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok6", role));
    }

    /**
     * Update role response entity.
     *
     * @param id   the id
     * @param role the role
     * @return the response entity
     */
    @PostMapping("/role/{roleId}")
    public ResponseEntity<ResponseDTO> updateRole(@PathVariable("roleId") String id, @RequestBody RoleEntity role) {
        RoleEntity roleUpdate = roleService.getById(Integer.parseInt(id));
        RoleEntity roleResponse = null;
        if (Objects.nonNull(roleUpdate) && !Objects.isNull(role)) {
            roleResponse = roleService.bindingRoleData(roleUpdate, role);
            roleUpdate.setUpdatedDate(new Date());
            roleService.saveOrUpdate(roleUpdate);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok7", roleResponse));
    }

    /**
     * Create role response entity.
     *
     * @param role the role
     * @return the response entity
     */
    @PostMapping("/role/addRole")
    public ResponseEntity<ResponseDTO> createRole(@RequestBody RoleEntity role) {
        RoleEntity roleEntity = new RoleEntity();
        RoleEntity roleResponse = null;
        if (Objects.nonNull(role)) {
            roleResponse = roleService.bindingRoleData(roleEntity, role);
            roleService.saveOrUpdate(roleEntity);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok8", roleResponse));
    }

    /**
     * Delete role response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/role/deleteRole/{roleId}")
    public ResponseEntity<ResponseDTO> deleteRole(@PathVariable("roleId") String id) {
        RoleEntity role = roleService.getById(Integer.parseInt(id));
        role.setStatus(false);
        roleService.saveOrUpdate(role);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted9", role));
    }

    // User actions

    /**
     * Gets user list.
     *
     * @return the user list
     */
    @GetMapping("/user/userList")
    public ResponseEntity<ResponseDTO> getUserList() {
        List<UserEntity> userList = userService.findAll();
        List<UserDTO> resUser = new ArrayList<>();
        for (UserEntity u : userList) {
            if (Boolean.TRUE.equals(u.getStatus())) {
                UserDTO userResponse = new UserDTO();
                userService.createUserDTO(userResponse, u);
                resUser.add(userResponse);
            }
        }
        return ResponseEntity.ok(new ResponseDTO(200, "get ok10", resUser));
    }

    /**
     * Gets user.
     *
     * @param userId the user id
     * @return the user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO> getUser1(@PathVariable("userId") String userId) {
        UserEntity user = userService.getById(Integer.parseInt(userId));
        UserDTO userResponse = new UserDTO();
        userService.createUserDTO(userResponse, user);
        return ResponseEntity.ok(new ResponseDTO(200, "get ok11", userResponse));
    }

    /**
     * Update user response entity.
     *
     * @param id   the id
     * @param user the user
     * @return the response entity
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable("userId") String id, @RequestBody UserDTO user) {
        UserEntity userUpdate = userService.getById(Integer.parseInt(id));
        UserDTO userResponse = null;
        if (Objects.nonNull(userUpdate) && !Objects.isNull(user)) {
            userResponse = userService.bindingUserData(userUpdate, user);
            if (user.getPassword() != null && user.getPassword().trim().isEmpty()) {
                userUpdate.setPassword(new BCryptPasswordEncoder(4).encode(user.getPassword()));
            }
            userUpdate.setUpdatedDate(new Date());
            userService.saveOrUpdate(userUpdate);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok12", userResponse));
    }

    /**
     * Create user response entity.
     *
     * @param user the user
     * @return the response entity
     */
    @PostMapping("/user/addUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO user) {
        UserEntity userEntity = new UserEntity();
        UserDTO userResponse = null;
        if (Objects.nonNull(user)) {
            userResponse = userService.bindingUserData(userEntity, user);
            userEntity.setPassword(new BCryptPasswordEncoder(4).encode("Abc@123"));
            userService.saveOrUpdate(userEntity);
        }
        for (RoleEntity role : userEntity.getRoles()) {
            UserRole ur = new UserRole();
            ur.setRoleId(role.getId());
            ur.setUserId(userEntity.getId());
            userRoleService.saveOrUpdate(ur);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", userResponse));
    }

    /**
     * Delete user response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/user/deleteUser/{userId}")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable("userId") String id) {
        UserEntity user = userService.getById(Integer.parseInt(id));
        user.setStatus(false);
        userService.saveOrUpdate(user);
        UserDTO userResponse = new UserDTO();
        userService.createUserDTO(userResponse, user);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted", userResponse));
    }
}
