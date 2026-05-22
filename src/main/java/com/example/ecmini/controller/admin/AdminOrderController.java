package com.example.ecmini.controller.admin;

import com.example.ecmini.entity.Order;
import com.example.ecmini.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    // 注文一覧
    @GetMapping
    public String list(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "admin/orders/list";
    }

    // 注文詳細
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.findById(id));
        return "admin/orders/detail";
    }

    // 発送
    @PostMapping("/{id}/ship")
    public String ship(@PathVariable Long id) {
        orderService.updateStatus(id, "SHIPPED");
        return "redirect:/admin/orders";
    }

    // キャンセル
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        orderService.updateStatus(id, "CANCELLED");
        return "redirect:/admin/orders";
    }
    //発送
    @GetMapping("/ship/{id}")
    public String shipOrder(@PathVariable Long id) {

        orderService.updateStatus(id, "SHIPPED");

        return "redirect:/admin/orders";
    }

}
