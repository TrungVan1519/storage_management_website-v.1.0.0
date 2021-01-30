package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.HistoryDAO;
import com.trungvan.entity.History;
import com.trungvan.entity.Invoice;
import com.trungvan.entity.Paging;

@Service
public class HistoryService {
	@Autowired
	private HistoryDAO<History> historyDAO;
	
	private static final Logger log = Logger.getLogger(HistoryService.class);
	
	public List<History> findAll(History history , Paging paging){
		
		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();
		
		if(history != null) {
			
			if(history.getProductInfo() != null) {
				
				if(!StringUtils.isEmpty(history.getProductInfo().getCategory().getName()) ) {
					
					query.append(" and model.productInfo.category.name like :categoryName");
					mapParams.put("categoryName","%" + history.getProductInfo().getCategory().getName() + "%");
				}
				
				if(!StringUtils.isEmpty(history.getProductInfo().getCode())) {
					
					query.append(" and model.productInfo.code = :code");
					mapParams.put("code", history.getProductInfo().getCode());
				}
				
				if( !StringUtils.isEmpty(history.getProductInfo().getName()) ) {
					
					query.append(" and model.productInfo.name like :name");
					mapParams.put("name", "%" + history.getProductInfo().getName() + "%");
				}
			}
			
			if( !StringUtils.isEmpty(history.getActionName()) ) {
				
				query.append(" and model.actionName like :actionName");
				mapParams.put("actionName", "%" + history.getActionName() + "%");
			}
			if(history.getType()!=0) {
				
				query.append(" and model.type = :type");
				mapParams.put("type", history.getType());
			}
		}
		return historyDAO.findAll(query.toString(), mapParams, paging);
	}
	
	public void save(Invoice invoice, String action) {
		
		log.info(">> Insert to history");
		
		History history = new History();
		history.setProductInfo(invoice.getProductInfo());
		history.setQuantity(invoice.getQuantity());
		history.setType(invoice.getType());
		history.setPrice(invoice.getPrice());
		history.setActiveFlag(1);
		history.setActionName(action);
		history.setCreatedDate(new Date());
		history.setUpdatedDate(new Date());
		historyDAO.save(history);
	}

}
