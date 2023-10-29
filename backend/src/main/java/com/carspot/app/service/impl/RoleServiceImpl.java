package com.carspot.app.service.impl;

import com.carspot.app.entity.ERole;
import com.carspot.app.entity.Role;
import com.carspot.app.repository.RoleRepository;
import com.carspot.app.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;


    @Override
    public Role findByName(ERole name) {
        return roleRepository.findByName(name);
    }
}
