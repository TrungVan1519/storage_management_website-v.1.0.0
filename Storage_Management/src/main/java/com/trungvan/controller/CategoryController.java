package com.trungvan.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.trungvan.service.CategoryService;
import com.trungvan.utils.Constant;
import com.trungvan.validation.CategoryValidator;

@Controller
@RequestMapping("/category")
public class CategoryController {
	
	private Logger log = Logger.getLogger(CategoryController.class);

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CategoryValidator categoryValidator;
	
	@InitBinder
	private void initBinder(WebDataBinder binder) {
		
		if(binder.getTarget() == null) return;
		
		// Dung de format string tu <form:hidden path="createdDate" /> sang kieu TimeStamp
		// 		thi moi update duoc category
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		if(binder.getTarget().getClass() == Category.class) {
			
			binder.setValidator(categoryValidator);
		}
	}
	
	// Retrieve
	@RequestMapping(value = { "/list", "/list/" })
	public String redirectToCategoryList() {
		
		return "redirect:/category/list/1";
	}
	
	/**
	 * > Do trong endpoint /category/list ta tao san Form tim kiem nen khong can phai tao 1 endpoint khac
	 * 		de show Form
	 * > Do khi truy van binh thuong den /category/list su dung GET, nhung neu tim kiem Category thi truy
	 * 		van den /category/list su dung POST nen o day khong chi dinh cu the @GetMapping hay @PostMapping duoc
	 * 		ma phai su dung @RequestMapping va ke ca khi su dung @RequestMapping ta cung khong duoc phep
	 * 		chi ro la su dung method=GET hay method=POST
	 * 
	 * @param model
	 * @param session
	 * @param category
	 * @return
	 */
	@RequestMapping("/list/{page}")
	public String showCategoryList(Model model, HttpSession session,
			@ModelAttribute("categorySearchForm") Category category,
			@PathVariable("page") int page) {
		
		if(session.getAttribute(Constant.MSG_SUCCESS) != null) {
			
			model.addAttribute(Constant.MSG_SUCCESS, session.getAttribute(Constant.MSG_SUCCESS));
			session.removeAttribute(Constant.MSG_SUCCESS);
		}
		
		if(session.getAttribute(Constant.MSG_FAILURE) != null) {
			
			model.addAttribute(Constant.MSG_FAILURE, session.getAttribute(Constant.MSG_FAILURE));
			session.removeAttribute(Constant.MSG_FAILURE);
		}
		
		log.info("categorySearchForm.id = "  + category.getId());
		log.info("categorySearchForm.code = "  + category.getCode());
		log.info("categorySearchForm.name = "  + category.getName());
		
		Paging paging = new Paging(3);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset
		
		model.addAttribute("categories", categoryService.findAll(category, paging));
		model.addAttribute("pageInfo", paging);
		return "categoryListView.definition";
	}
	
	@GetMapping("/view/{categoryId}")
	public String showSingleCategory(Model model, @PathVariable("categoryId") int categoryId) {
		
		log.info(">> View category with id: " + categoryId);
		
		Category category = categoryService.findById(categoryId);
		if(category != null) {

			model.addAttribute("formTitle", "View selected category");
			model.addAttribute("viewMode", true); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("categoryForm", category);
			
			return "categoryAction.definition";
		}
		return "redirect:/category/list";
	}
	
	// Create and Update
	@GetMapping("/add")
	public String addCategory(Model model) {
		
		log.info(">> Add new category:");
		
		model.addAttribute("formTitle", "Add new category");
		model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
		model.addAttribute("categoryForm", new Category());
		
		return "categoryAction.definition";
	}
	
	@GetMapping("/edit/{categoryId}")
	public String editCategory(Model model, @PathVariable("categoryId") int categoryId) {
		
		log.info(">> Edit category with id: " + categoryId);
		
		Category category = categoryService.findById(categoryId);
		if(category != null) {

			model.addAttribute("formTitle", "Edit selected category");
			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			model.addAttribute("categoryForm", category);
			
			return "categoryAction.definition";
		}
		return "redirect:/category/list";
	}
	
	@PostMapping("/save")
	public String saveCategory(Model model,
			@Validated @ModelAttribute("categoryForm") Category category,
			BindingResult bindingResult,
			HttpSession session) {
		
		log.info(">> Confirm category: " + category.getName());
		
		if(bindingResult.hasErrors()) {
			
			if(category.getId() != null || category.getId() != 0) {

				model.addAttribute("formTitle", "Edit selected category");
			} else {
				
				model.addAttribute("formTitle", "Add new category");
			}

			model.addAttribute("viewMode", false); // > Dung de cho phep user chi duoc xem hay khong
			
			return "categoryAction.definition";
		}
		
		// TH su dung Form de update vi id != 0, null co nghia category da co trong DB
		if(category.getId() != null || category.getId() != 0) {
			
			try {
				categoryService.updateCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Update category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Update category failed!");
				e.printStackTrace();
			}
			log.info("<<>> UPDATE SUCCESS");
		}
		// TH su dung Form de add
		else {

			try {
				categoryService.saveCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Add category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Add category failed!");
				e.printStackTrace();
			}
			log.info("<<>> ADD SUCCESS");
		}
		
		return "redirect:/category/list"; 
	}
	
	// Delete
	@GetMapping("/delete/{categoryId}")
	public String deleteCategory(Model model, 
			@PathVariable("categoryId") int categoryId,
			HttpSession session) {

		log.info(">> Delete category with id: " + categoryId);
		
		Category category = categoryService.findById(categoryId);
		if(category != null) {

			try {
				categoryService.deleteCategory(category);
				session.setAttribute(Constant.MSG_SUCCESS, "Delete category successfully!");
				
			} catch (Exception e) {

				session.setAttribute(Constant.MSG_FAILURE, "Delete category failed!");
				e.printStackTrace();
			}
		}
		
		return "redirect:/category/list";
	}
}
