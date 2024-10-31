package com.example.restaurant.controller;

import com.example.restaurant.dto.ResponseDTO;
import com.example.restaurant.model.RoleEntity;
import com.example.restaurant.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roleList")
    public ResponseEntity<ResponseDTO> getRoleList() {
        List<RoleEntity> roleList = roleService.findAll();
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", roleList));
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<ResponseDTO> getUser(@PathVariable("roleId") String roleId) {
        RoleEntity role = roleService.getById(Integer.parseInt(roleId));
        System.out.println("hehe" + role);
        return ResponseEntity.ok(new ResponseDTO(200, "get ok", role));
    }

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
}
