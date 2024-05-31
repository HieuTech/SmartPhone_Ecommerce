package org.example.projectmd3_smartphone_ecommerce.controller;


import org.example.projectmd3_smartphone_ecommerce.dto.response.AuthenResponse;

import org.example.projectmd3_smartphone_ecommerce.entity.Vouchers;
import org.example.projectmd3_smartphone_ecommerce.service.*;

import org.example.projectmd3_smartphone_ecommerce.entity.Products;

import org.example.projectmd3_smartphone_ecommerce.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductServiceImpl productService2;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WishListService wishListService;
    @Autowired
    private VoucherService voucherService;
    private
    @Autowired
    HttpSession session;

//    @GetMapping("")
//    public String home(Model model,@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "5") int size){
//        session.setAttribute("user", userService.findByIdV2(1));
//        model.addAttribute("productList", productService2.selectAllProducts(currentPage,size));
//        model.addAttribute("totalPages",Math.ceil( (double) productService2.countAllProduct() / size));
//        model.addAttribute("title", "Latest Products");
//        return "Client/home/home";
//    }



//    @GetMapping("/dao")
//    public String home(Model model, @RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "4") int size) {
//        session.setAttribute("user", userService.findByIdV2(1));
//        model.addAttribute("productList", productService2.selectAllProducts(currentPage, size));
//        model.addAttribute("totalPages", Math.ceil((double) productService2.countAllProduct() / size));
//        model.addAttribute("title", "Latest Products");
//        return "Client/home/home";
//    }

    @GetMapping
    public String home(Model model, @RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "4") int size) {

        model.addAttribute("message", "");

        model.addAttribute("productList", productService2.selectAllProducts(currentPage, size));
        return "Client/home/home";

    }

    @PostMapping("/Filter/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        session.setAttribute("user", userService.findByIdV2(1));
        model.addAttribute("productList", productService2.searchProduct(keyword));
        return "Client/home/Filter";
    }

    @Autowired
    CategoriesService categoriesService;

    @GetMapping("/Filter")
    public String FilterInit(Model model, @RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "4") int size) {
        session.setAttribute("user", userService.findByIdV2(1));
        model.addAttribute("productList", productService2.selectAllProducts(currentPage, size));
        model.addAttribute("totalPages", Math.ceil((double) productService2.countAllProduct() / size));
        model.addAttribute("categories", categoriesService.getAll(0,100));
        return "Client/home/Filter";
    }

    @RequestMapping("/Filter/{id}")
    public String Filter(Model model, @RequestParam(defaultValue = "0") int currentPage,@RequestParam(defaultValue = "4") int size,@PathVariable(name = "id", required = false) Integer id) {
        session.setAttribute("user", userService.findByIdV2(1));
        model.addAttribute("productList", productService2.FilterByCat(currentPage,size,id));
        model.addAttribute("totalPages",  Math.ceil((double) productService2.countAllProduct() / size));
        model.addAttribute("categories", categoriesService.getAll(0,100));
//        model.addAttribute("category", categoriesService.findById(id));

        return "Client/home/Filter";
    }


    @GetMapping("/orderHistory")
    public String orderHistory(Model model) {
        session.setAttribute("user", userService.findByIdV2(1));
        return "Client/orders/orderHistory";
    }


    @PostMapping("/getVoucher")
    public String getVoucher(Model model, @RequestParam("emailVoucher") String emailVoucher) {
        model.addAttribute("productList", productService.findAllV2());

        if (!this.voucherService.addNew(emailVoucher)) {
            model.addAttribute("message", "Your Email Got Voucher Already Or Wrong Email");
        } else {
            model.addAttribute("message", "Voucher successfully added! Check your email.");
        }
        return "Client/home/home";
    }

    @GetMapping("/contactUs")
    public String contactUs(Model model) {
        return "Client/home/contactus";
    }

    @PostMapping("/contactUs")
    public String doContactUs(Model model) {
        return "Client/home/contactus";
    }


}
