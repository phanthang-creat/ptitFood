package com.server.ptitFood.controllers.admin;

import com.server.ptitFood.domain.entities.OrderDetail;
import com.server.ptitFood.domain.entities.Product;
import com.server.ptitFood.domain.services.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "admin")
public class AdminHome {

    private final DashboardService dashboardService;

    public AdminHome(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RequestMapping("/home")
    public String home(
            Model model
    ) {
        model.addAttribute("countOrder", dashboardService.countOrder());
        model.addAttribute("totalMoney", dashboardService.totalMoney());
        model.addAttribute("todayCount", dashboardService.todayCount());
        model.addAttribute("todayMoney", dashboardService.todayMoney());

        List<Object[]> listMoneyByMonth = dashboardService.getMoneyByMonth();
        for (Object[] objects : listMoneyByMonth) {
            System.out.println(objects[0]);
            System.out.println(objects[1]);
            System.out.println(objects[2]);
        }
        model.addAttribute("listMoneyByMonth", listMoneyByMonth);
        return "web/admin/home/dashboard";
    }
}
