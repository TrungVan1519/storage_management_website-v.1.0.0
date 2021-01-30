package com.trungvan.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.entity.Category;
import com.trungvan.entity.Paging;
import com.trungvan.entity.ProductInfo;
import com.trungvan.service.CategoryService;
import com.trungvan.service.ProductInfoService;
import com.trungvan.utils.Constant;
import com.trungvan.validation.ProductInfoValidator;

@Controller
@RequestMapping("/product-info")
public class ProductInfoController {
	
	private Logger log = Logger.getLogger(ProductInfoController.class);
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductInfoService productInfoService;
	
	@Autowired
	private ProductInfoValidator productInfoValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		if(binder.getTarget() == null) return;
		
		// Dung de format string tu <form:hidden path="createdDate" /> sang kieu TimeStamp
		// 		thi moi update duoc productInfo
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		if(binder.getTarget().getClass() == ProductInfo.class) {
			
			binder.setValidator(productInfoValidator);
		}
	}
	
	// Retrieve
	@RequestMapping(value = { "/list", "/list/" })
	public String redirectToProductInfoList() {
		
		return "redirect:/product-info/list/1";
	}
	
	/**
	 * > Do trong endpoint /product-info/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /product-info/list su dung GET, nhung neu tim kiem productInfo thi truy
	 * 		van den /product-info/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param productInfo
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public String showProductInfoList(Model model, HttpSession session,
			@ModelAttribute("productInfoSearchForm") ProductInfo productInfo,
			@PathVariable("page") int page) {
		
		if(session.getAttribute(Constant.MSG_SUCCESS) != null) {
			
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		
		if(session.getAttribute(Constant.MSG_FAILURE) != null) {
			
			model.addAttribute(Constant.MSG_FAILURE, session.getAttribute(Constant.MSG_FAILURE));
			session.removeAttribute(Constant.MSG_FAILURE);
		}
		
		log.info("productInfoSearchForm.id = "  + productInfo.getId());
		log.info("productInfoSearchForm.code = "  + productInfo.getCode());
		log.info("productInfoSearchForm.name = "  + productInfo.getName());
		
		Paging paging = new Paging(3);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset
		
		model.addAttribute("productInfos", productInfoService.findAll(productInfo, paging));
		model.addAttribute("pageInfo", paging);
		
		return "productInfoListView.definition";
	}
	
	@GetMapping("/view/{productInfoId}")
	public String showSingleProductInfo(Model model, @PathVariable("productInfoId") int productInfoId) {
		
		log.info(">> View productInfo with id: " + productInfoId);
		
		ProductInfo productInfo = productInfoService.findByProductInfoId(productInfoId);
		if(productInfo != null) {

			model.addAttribute("formTitle", "View selected productInfo");
			model.addAttribute("viewMode", true); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("productInfoForm", productInfo);
			
			return "productInfoAction.definition";
		}
		return "redirect:/product-info/list";
	}
	
	// Create and Update
	@GetMapping("/add")
	public String addProductInfo(Model model) {
		
		log.info(">> Add new productInfo:");
		
		model.addAttribute("formTitle", "Add new productInfo");
		model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
		model.addAttribute("productInfoForm", new ProductInfo());
		
		// Them productInfo vao category 
		List<Category> categories = categoryService.findAll(null, null);
		Map<String, String> mapCategory = new HashMap<String, String>();
		for(Category category : categories) {
			
			mapCategory.put(String.valueOf(category.getId()), category.getName());
		}
		model.addAttribute("mapCategory", mapCategory);
		
		return "productInfoAction.definition";
	}
	
	@GetMapping("/edit/{productInfoId}")
	public String editProductInfo(Model model, @PathVariable("productInfoId") int productInfoId) {
		
		log.info(">> Edit productInfo with id: " + productInfoId);
		
		ProductInfo productInfo = productInfoService.findByProductInfoId(productInfoId);
		if(productInfo != null) {

			// > Thiet lap category da co san trong productInfo de hien thi trong Form Edit
			productInfo.getCategory().setId(productInfo.getCategory().getId());
			
			model.addAttribute("formTitle", "Edit selected productInfo");
			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("productInfoForm", productInfo);
			
			// Sua productInfo vao category khac
			List<Category> categories = categoryService.findAll(null, null);
			Map<String, String> mapCategory = new HashMap<String, String>();
			for(Category category : categories) {
				
				mapCategory.put(String.valueOf(category.getId()), category.getName());
			}
			model.addAttribute("mapCategory", mapCategory);
			
			return "productInfoAction.definition";
		}
		return "redirect:/product-info/list";
	}
	
	@PostMapping("/save")
	public String saveProductInfo(Model model,
			@Validated @ModelAttribute("productInfoForm") ProductInfo productInfo,
			BindingResult bindingResult,
			HttpSession session) {
		
		log.info(">> Confirm productInfo.id: " + productInfo.getId());
		log.info(">> Confirm productInfo.name: " + productInfo.getName());
		log.info(">> Confirm productInfo.category.id: " + productInfo.getCategory().getId());
		log.info(">> Confirm productInfo.category.name: " + productInfo.getCategory().getName());
		log.info(">> Confirm productInfo.category.code: " + productInfo.getCategory().getCode());
		
		if(bindingResult.hasErrors()) {
			
			for(ObjectError e: bindingResult.getAllErrors()) {

				log.info(">> bindingResult.e: " + e);
			}
			
			if(productInfo.getId() != null && productInfo.getId() != 0) {

				model.addAttribute("formTitle", "Edit selected productInfo");
			} else {
				
				model.addAttribute("formTitle", "Add new productInfo");
			}

			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			
			// Show lai productInfo trong category cu khi co loi xay ra
			List<Category> categories = categoryService.findAll(null, null);
			Map<String, String> mapCategory = new HashMap<String, String>();
			for(Category category : categories) {
				
				mapCategory.put(String.valueOf(category.getId()), category.getName());
			}
			model.addAttribute("mapCategory", mapCategory);
			
			return "productInfoAction.definition";
		}
		
		// > Do ta chi nhan ve gia tri id cua object category trong productInfo nen ta chi lay ra va
		//		su dung duoc moi prop nay 
		// > Sau do ta se phai tim kiem lai category trong danh sach category = prop id nay de lay ra
		//		va luc nay moi truyen duoc vao propductInfo
//		Category category = categoryService.findById(productInfo.getCategory().getId());
//		productInfo.setCategory(category);
		
//		log.info(">> category chosen by options: " + category);
		
		// TH su dung Form de update vi id != 0, null co nghia productInfo da co trong DB
		if(productInfo.getId() != null && productInfo.getId() != 0) {
			
			try {
				productInfoService.updateProductInfo(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Update productInfo successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Update productInfo failed!");
				e.printStackTrace();
			}
			log.info("<<>> UPDATE SUCCESS");
		}
		// TH su dung Form de add
		else {

			try {
				productInfoService.saveProductInfo(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Add productInfo successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Add productInfo failed!");
				e.printStackTrace();
			}
			log.info("<<>> ADD SUCCESS");
		}
		
		return "redirect:/product-info/list"; 
	}
	
	// Delete
	@GetMapping("/delete/{productInfoId}")
	public String deleteProductInfo(Model model, 
			@PathVariable("productInfoId") int productInfoId,
			HttpSession session) {

		log.info(">> Delete productInfo with id: " + productInfoId);
		
		ProductInfo productInfo = productInfoService.findByProductInfoId(productInfoId);
		if(productInfo != null) {

			try {
				productInfoService.deleteProductInfo(productInfo);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete productInfo successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Delete productInfo failed!");
				e.printStackTrace();
			}
		}
		
		return "redirect:/product-info/list";
	}
}
