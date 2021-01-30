package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.CategoryDAO;
import com.trungvan.dao.ProductInfoDAO;
import com.trungvan.entity.Category;
import com.trungvan.entity.Paging;
import com.trungvan.entity.ProductInfo;

@Service
public class CategoryService {
	
	private static Logger log = Logger.getLogger(CategoryService.class);

	@Autowired
	private CategoryDAO<Category> categoryDAO;
	
//	@Autowired
//	private ProductInfoDAO<ProductInfo> productInfoDAO;
	
	// Service for categoryDAO
	public void saveCategory(Category category) throws Exception {
		
		category.setActiveFlag(1);
		category.setCreatedDate(new Date());
		category.setUpdatedDate(new Date());
		
		log.info(">> Save category: " + category);
		
		categoryDAO.save(category);
	}
	
	public void updateCategory(Category category) throws Exception {
		
		category.setUpdatedDate(new Date());
		
		log.info(">> Update category: " + category);

		categoryDAO.update(category);
	}
	
	public void deleteCategory(Category category) throws Exception {
		
		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
		category.setActiveFlag(0);
		category.setUpdatedDate(new Date());
		
		log.info(">> Delete category (activeFlag = 0):" + category);
		
		categoryDAO.update(category);
	}
	
	public List<Category> findAll(Category category, Paging paging) {
		
		log.info(">> Find all: ");
		
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		
		if(category != null) {
			
			if(category.getId() != null && category.getId() != 0) {
				
				query.append(" and model.id = :id");
				mapParams.put("id", category.getId());
			} 
			
			if(category.getName() != null && !StringUtils.isEmpty(category.getName())) {
				
				query.append(" and model.name = like :name");
				mapParams.put("name", "%" + category.getName() + "%");
			}
			
			if(category.getCode() != null && !StringUtils.isEmpty(category.getCode())) {
				
				query.append(" and model.code = :code");
				mapParams.put("code", category.getCode());
			}
		}
		
		return categoryDAO.findAll(query.toString(), mapParams, paging);
	}
	
	public Category findById(int id) {
		
		log.info(">> Find by id: " + id);
		return categoryDAO.findById(Category.class, id);
	}
	
	public List<Category> findByProperty(String property, Object value){
		
		log.info(">> Find by property: " + property + "\tvalue: " + value.toString());
		return categoryDAO.findByProperty(property, value);
	}
	
//	// Service for productInfoDAO
//	public void saveProductInfo(ProductInfo productInfo) throws Exception {
//		
//		productInfo.setActiveFlag(1);
//		productInfo.setCreatedDate(new Date());
//		productInfo.setUpdatedDate(new Date());
//		productInfo.setImageUrl("/upload/" + productInfo.getMultipartFile().getOriginalFilename());
//		
//		log.info(">> Save ProductInfo: " + productInfo);
//		
//		productInfoDAO.save(productInfo);
//	}
//	
//	public void updateProductInfo(ProductInfo productInfo) throws Exception {
//		
//		productInfo.setUpdatedDate(new Date());
//		
//		log.info(">> Update ProductInfo: " + productInfo);
//
//		productInfoDAO.update(productInfo);
//	}
//	
//	public void deleteProductInfo(ProductInfo productInfo) throws Exception {
//		
//		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
//		productInfo.setActiveFlag(0);
//		productInfo.setUpdatedDate(new Date());
//		
//		log.info(">> Delete ProductInfo (activeFlag = 0):" + productInfo);
//		
//		productInfoDAO.update(productInfo);
//	}
//	
//	public List<ProductInfo> findAll(ProductInfo productInfo, Paging paging) {
//		
//		log.info(">> Find all: ");
//		
//		StringBuilder query = new StringBuilder();
//		Map<String, Object> mapParams = new HashMap<String, Object>();
//		
//		if(productInfo != null) {
//			
//			if(productInfo.getId() != null && productInfo.getId() != 0) {
//				
//				query.append(" and model.id = :id");
//				mapParams.put("id", productInfo.getId());
//			} 
//			
//			if(productInfo.getName() != null && !StringUtils.isEmpty(productInfo.getName())) {
//				
//				query.append(" and model.name = like :name");
//				mapParams.put("name", "%" + productInfo.getName() + "%");
//			}
//			
//			if(productInfo.getCode() != null && !StringUtils.isEmpty(productInfo.getCode())) {
//				
//				query.append(" and model.code = :code");
//				mapParams.put("code", productInfo.getCode());
//			}
//		}
//		
//		return productInfoDAO.findAll(query.toString(), mapParams, paging);
//	}
//	
//	public ProductInfo findByProductInfoId(int id) {
//		
//		log.info(">> Find by id: " + id);
//		return productInfoDAO.findById(ProductInfo.class, id);
//	}
//	
//	public List<ProductInfo> findByProductInfoProperty(String property, Object value){
//		
//		log.info(">> Find by property: " + property + "\tvalue: " + value.toString());
//		return productInfoDAO.findByProperty(property, value);
//	}
}
