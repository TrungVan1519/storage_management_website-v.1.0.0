package com.trungvan.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.entity.Paging;
import com.trungvan.entity.ProductInStock;
import com.trungvan.service.ProductInStockService;

@Controller
@RequestMapping("/product-in-stock")
public class ProductInStockController {

	static final Logger log = Logger.getLogger(ProductInStockController.class);
	
	@Autowired
	private ProductInStockService productInStockService;
	
	@GetMapping(value = {"/list","/list/"})
	public String redirect() {
		
		return "redirect:/product-in-stock/list/1";
	}
	
	@RequestMapping(value="/list/{page}")
	public String list(Model model, 
			@ModelAttribute("searchForm") ProductInStock productInStock,
			@PathVariable("page") int page) {

		Paging paging = new Paging(3);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset
		
		model.addAttribute("products", productInStockService.findAll(productInStock, paging));
		model.addAttribute("pageInfo", paging);
		
		return "productInStockView.definition";
	}
}
