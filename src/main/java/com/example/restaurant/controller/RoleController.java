package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * The type Role controller.
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;

    /**
     * Instantiates a new Role controller.
     *
     * @param roleService the role service
     */
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Gets role list.
     *
     * @return the role list
     */
    @GetMapping("/roleList")
    public ResponseEntity<ResponseDTO> getRoleList() {
        List<RoleEntity> roleList = roleService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", roleList));
    }

    /**
     * Gets user.
     *
     * @param roleId the role id
     * @return the user
     */
    @GetMapping("/{roleId}")
    public ResponseEntity<ResponseDTO> getUser(@PathVariable("roleId") String roleId) {
        RoleEntity role = roleService.getById(Integer.parseInt(roleId));
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", role));
    }

    /**
     * Update role response entity.
     *
     * @param id   the id
     * @param role the role
     * @return the response entity
     */
    @PostMapping("/{roleId}")
    public ResponseEntity<ResponseDTO> updateRole(@PathVariable("roleId") String id, @RequestBody RoleEntity role) {
        RoleEntity roleUpdate = roleService.getById(Integer.parseInt(id));
        RoleEntity roleResponse = null;
        if (Objects.nonNull(roleUpdate) && !Objects.isNull(role)) {
            roleResponse = roleService.bindingRoleData(roleUpdate, role);
            roleUpdate.setUpdatedDate(new Date());
            roleService.saveOrUpdate(roleUpdate);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", roleResponse));
    }

    /**
     * Create role response entity.
     *
     * @param role the role
     * @return the response entity
     */
    @PostMapping("/addRole")
    public ResponseEntity<ResponseDTO> createRole(@RequestBody RoleEntity role) {
        RoleEntity roleEntity = new RoleEntity();
        RoleEntity roleResponse = null;
        if (Objects.nonNull(role)) {
            roleResponse = roleService.bindingRoleData(roleEntity, role);
            roleService.saveOrUpdate(roleEntity);
        }
        return ResponseEntity.ok(new ResponseDTO(200, "update ok", roleResponse));
    }

    /**
     * Delete role response entity.
     *
     * @param id the id
     * @return the response entity
     */
    @PostMapping("/deleteRole/{roleId}")
    public ResponseEntity<ResponseDTO> deleteRole(@PathVariable("roleId") String id) {
        RoleEntity role = roleService.getById(Integer.parseInt(id));
        role.setStatus(false);
        roleService.saveOrUpdate(role);
        return ResponseEntity.ok(new ResponseDTO(200, "deleted", role));
    }
}
