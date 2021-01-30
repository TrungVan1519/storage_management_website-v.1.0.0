package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.ProductInStockDAO;
import com.trungvan.entity.Invoice;
import com.trungvan.entity.Paging;
import com.trungvan.entity.ProductInStock;
import com.trungvan.entity.ProductInfo;

@Service
public class ProductInStockService {
	
	private static final Logger log = Logger.getLogger(ProductInStockService.class);
	
	@Autowired
	private ProductInStockDAO<ProductInStock> productInStockDAO;
	
	public List<ProductInStock> findAll(ProductInStock productInStock,Paging paging){
		
		log.info(">> Product in stock lít:");
		
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(productInStock != null && productInStock.getProductInfo() != null) {
			
			if(!StringUtils.isEmpty(productInStock.getProductInfo().getCategory().getName())) {
				
				query.append(" and model.productInfo.category.name like :categoryName");
				mapParams.put("categoryName","%" + productInStock.getProductInfo().getCategory().getName() + "%");
			}
			if(productInStock.getProductInfo().getCode() != null && !StringUtils.isEmpty(productInStock.getProductInfo().getCode())) {
		
				query.append(" and model.productInfo.code = :code");
				mapParams.put("code", productInStock.getProductInfo().getCode());
			}
			if(productInStock.getProductInfo().getName()!=null && !StringUtils.isEmpty(productInStock.getProductInfo().getName()) ) {
			
				query.append(" and model.productInfo.name like :name");
				mapParams.put("name", "%" + productInStock.getProductInfo().getName() + "%");
			}
		}
		
		return productInStockDAO.findAll(query.toString(), mapParams, paging);
	}
	
	/**
	 * > Dung khi nhap, xuat productInStock thi dong thoi cung thay doi luon invoice de tu do 
	 * 		cap nhap so luong productInStock
	 * > invoice chi xu ly 2 van de nhap productInStock va xuat productInStock thong qua prop type: 
	 * 		+ type = 1: nhap productInStock va invoice.getQuantity() > 0
	 * 		+ type = 2: xuat productInStock va invoice.getQuantity() < 0
	 * @param invoice
	 * @throws Exception
	 */
	public void saveOrUpdate(Invoice invoice) throws Exception{
		
		log.info(">> Save product in stock ");
		
		if(invoice.getProductInfo() != null) {
			log.info("product in stock ");
			if(invoice.getProductInfo()!=null) {
				int id = invoice.getProductInfo().getId();
				List<ProductInStock>  products= productInStockDAO.findByProperty("productInfo.id", id);
				ProductInStock product=null;
				if(products!=null && !products.isEmpty()) {
					product = products.get(0);
					log.info("update qty="+invoice.getQuantity()+" and price="+invoice.getPrice());
					product.setQuantity(product.getQuantity()+invoice.getQuantity());
					// type =1 receipt , type =2 issues
					if(invoice.getType()==1) {
						product.setPrice(invoice.getPrice());
					}
					product.setUpdatedDate(new Date());
					productInStockDAO.update(product);
				
				}else if(invoice.getType()==1){
					log.info("insert to stock qty="+invoice.getQuantity()+" and price="+invoice.getPrice());
					product = new ProductInStock();
					ProductInfo productInfo = new ProductInfo();
					productInfo.setId(invoice.getProductInfo().getId());
					product.setProductInfo(productInfo);
					product.setActiveFlag(1);
					product.setCreatedDate(new Date());
					product.setUpdatedDate(new Date());
					product.setQuantity(invoice.getQuantity());
					product.setPrice(invoice.getPrice());
					productInStockDAO.save(product);
				}
			}
		}
	}

}
