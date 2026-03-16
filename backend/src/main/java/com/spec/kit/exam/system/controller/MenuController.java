package com.spec.kit.exam.system.controller;

import com.spec.kit.exam.system.annotation.PermissionRequired;
import com.spec.kit.exam.system.entity.Menu;
import com.spec.kit.exam.system.service.MenuService;
import com.spec.kit.exam.system.util.Result;
import com.spec.kit.exam.system.enums.MenuErrorCodeEnum;
import com.spec.kit.exam.system.util.PageRequestDTO;
import com.spec.kit.exam.system.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController extends BaseController {

    private final MenuService menuService;

    @PermissionRequired(menu = "menu-management", operation = "READ")
    @PostMapping("/list")
    public Result<PageResponse<Menu>> list(@RequestBody(required = false) PageRequestDTO request) {
        PageRequestDTO pageRequest = request == null ? new PageRequestDTO() : request;
        List<Menu> menus = menuService.getAllMenus();
        int totalCount = menus.size();

        int startIndex = (pageRequest.getPage() - 1) * pageRequest.getSize();
        if (startIndex >= totalCount) {
            menus = new java.util.ArrayList<>();
        } else {
            int endIndex = Math.min(startIndex + pageRequest.getSize(), totalCount);
            menus = menus.subList(startIndex, endIndex);
        }

        PageResponse<Menu> pageResponse = PageResponse.of(menus, totalCount, pageRequest.getPage(), pageRequest.getSize());
        return successList(pageResponse);
    }

    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/tree")
    public Result<List<Menu>> getMenuTree(@RequestParam(required = false) String roleId) {
        List<Menu> menuTree = menuService.getMenuTreeByRole(roleId);
        return successList(menuTree);
    }

    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/{id}")
    public Result<Menu> getById(@PathVariable String id) {
        Menu menu = menuService.getMenuById(id);
        if (menu != null) {
            return successDetail(menu);
        } else {
            return Result.error(MenuErrorCodeEnum.MENU_NOT_FOUND);
        }
    }

    @PermissionRequired(menu = "menu-management", operation = "CREATE")
    @PostMapping
    public Result<Menu> create(@RequestBody Menu menu) {
        Menu createdMenu = menuService.createMenu(menu);
        if (createdMenu != null) {
            return successCreate(createdMenu);
        } else {
            return Result.error(MenuErrorCodeEnum.MENU_ALREADY_EXISTS);
        }
    }

    @PermissionRequired(menu = "menu-management", operation = "UPDATE")
    @PutMapping("/{id}")
    public Result<Menu> update(@PathVariable String id, @RequestBody Menu menu) {
        menu.setId(id);
        boolean success = menuService.updateMenu(menu);
        if (success) {
            return successUpdate(menu);
        } else {
            return Result.error(MenuErrorCodeEnum.FAILED_TO_UPDATE_MENU, "菜单更新失败");
        }
    }

    @PermissionRequired(menu = "menu-management", operation = "DELETE")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        boolean success = menuService.deleteMenu(id);
        if (success) {
            return successDelete();
        } else {
            return Result.error(MenuErrorCodeEnum.FAILED_TO_DELETE_MENU, "菜单删除失败");
        }
    }

    @PermissionRequired(menu = "menu-management", operation = "READ")
    @GetMapping("/statistics")
    public Result<java.util.Map<String, Object>> getStatistics() {
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        List<Menu> allMenus = menuService.getAllMenus();
        statistics.put("total", allMenus.size());
        statistics.put("parentMenus", allMenus.stream().filter(m -> m.getParentId() == null).count());
        statistics.put("childMenus", allMenus.stream().filter(m -> m.getParentId() != null).count());
        return successStatistics(statistics);
    }
}