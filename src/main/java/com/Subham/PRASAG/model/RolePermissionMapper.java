package com.Subham.PRASAG.model;

import java.util.Set;
//final means this class cannot be extended.
public  final class RolePermissionMapper {

    RolePermissionMapper() {}
    public static Set<Permission> permission(Role role)
    {
        return switch (role)
        {
            case ADMIN->Set.of(Permission.ADMIN_WRITE, Permission.ADMIN_READ);
            case USER->Set.of(Permission.USER_READ,Permission.USER_WRITE);
        };
    }
}
