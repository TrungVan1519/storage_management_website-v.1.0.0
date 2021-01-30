package com.trungvan.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.trungvan.dao.ProductInfoDAO;
import com.trungvan.entity.Paging;
import com.trungvan.entity.ProductInfo;
import com.trungvan.utils.Constant;

@Service
public class ProductInfoService {
	
	private static Logger log = Logger.getLogger(ProductInfoService.class);

	@Autowired
	private ProductInfoDAO<ProductInfo> productInfoDAO;
	
	// Service for productInfoDAO
	public void saveProductInfo(ProductInfo productInfo) throws Exception {
		
		log.info(">> Save ProductInfo: " + productInfo);
		
		productInfo.setActiveFlag(1);
		productInfo.setCreatedDate(new Date());
		productInfo.setUpdatedDate(new Date());
		// TH them moi image thi dong thoi cung them imageUrl vao DB
		// /upload/ == /upload/**
		String fileName = System.currentTimeMillis() + "_" + productInfo.getMultipartFile().getOriginalFilename();
		uploadFile(productInfo.getMultipartFile(), fileName);
		productInfo.setImageUrl("/upload/" + fileName);
		
		productInfoDAO.save(productInfo);
	}
	
	public void updateProductInfo(ProductInfo productInfo) throws Exception {
		
		log.info(">> Update ProductInfo: " + productInfo);
		
		productInfo.setUpdatedDate(new Date());
		if(!productInfo.getMultipartFile().getOriginalFilename().isEmpty()) {

			// TH update image khac thi thiet lap lai imageUrl trong DB
			// /upload/ == /upload/**
			String fileName = System.currentTimeMillis() + "_" + productInfo.getMultipartFile().getOriginalFilename();
			uploadFile(productInfo.getMultipartFile(), fileName);
			productInfo.setImageUrl("/upload/" + fileName);
		}

		productInfoDAO.update(productInfo);
	}
	
	public void deleteProductInfo(ProductInfo productInfo) throws Exception {
		
		log.info(">> Delete ProductInfo (activeFlag = 0):" + productInfo);
		
		// Do khi xoa ta khong xoa han object ma chi set activeFlag = 0
		productInfo.setActiveFlag(0);
		productInfo.setUpdatedDate(new Date());
		
		productInfoDAO.update(productInfo);
	}
	
	public List<ProductInfo> findAll(ProductInfo productInfo, Paging paging) {
		
		log.info(">> Find all: ");
		
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		
		if(productInfo != null) {
			
			if(productInfo.getId() != null && productInfo.getId() != 0) {
				
				query.append(" and model.id = :id");
				mapParams.put("id", productInfo.getId());
			} 
			
			if(productInfo.getName() != null && !StringUtils.isEmpty(productInfo.getName())) {
				
				query.append(" and model.name = like :name");
				mapParams.put("name", "%" + productInfo.getName() + "%");
			}
			
			if(productInfo.getCode() != null && !StringUtils.isEmpty(productInfo.getCode())) {
				
				query.append(" and model.code = :code");
				mapParams.put("code", productInfo.getCode());
			}
		}
		
		return productInfoDAO.findAll(query.toString(), mapParams, paging);
	}
	
	public ProductInfo findByProductInfoId(int id) {
		
		log.info(">> Find by id: " + id);
		return productInfoDAO.findById(ProductInfo.class, id);
	}
	
	public List<ProductInfo> findByProductInfoProperty(String property, Object value){
		
		log.info(">> Find by property: " + property + "\tvalue: " + value.toString());
		return productInfoDAO.findByProperty(property, value);
	}
	
	private void uploadFile(MultipartFile multipartFile, String fileName) throws IllegalStateException, IOException {
		
		if(!multipartFile.getOriginalFilename().isEmpty()) {

			//"E:\\1_Coding\\1_Complete\\0_Real_Project\\Storage_Management_Project\\Storage_Management\\fileupload\\"
			File file = new File(Constant.SERVER_STATIC_RESOURCES + fileName);
			multipartFile.transferTo(file);
		}
	}
}