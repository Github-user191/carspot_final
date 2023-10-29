package com.carspot.app.service;

import com.carspot.app.entity.ERole;
import com.carspot.app.entity.Role;


public interface RoleService {
    Role findByName(ERole name);
}
