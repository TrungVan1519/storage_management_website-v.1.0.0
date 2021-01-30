package com.trungvan.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.trungvan.dao.InvoiceDAO;
import com.trungvan.entity.Invoice;
import com.trungvan.entity.Paging;
import com.trungvan.utils.Constant;

@Service
public class InvoiceService {

	static final Logger log = Logger.getLogger(InvoiceService.class);

	@Autowired
	private HistoryService historyService;

	@Autowired
	private ProductInStockService productInStockService;

	@Autowired
	private InvoiceDAO<Invoice> invoiceDAO;

	public void save(Invoice invoice) throws Exception {

		invoice.setActiveFlag(1);
		invoice.setCreatedDate(new Date());
		invoice.setUpdatedDate(new Date());

		invoiceDAO.save(invoice);

		// > Luu vao lich su
		historyService.save(invoice, Constant.ACTION_ADD);

		// > Cap nhat ve so luong, loai hang hoa, ...
		productInStockService.saveOrUpdate(invoice);
	}

	public void update(Invoice invoice) throws Exception {

		invoice.setUpdatedDate(new Date());

		// > Lay ra so luong hang hoa hien tai
		int originQty = invoiceDAO.findById(Invoice.class, invoice.getId()).getQuantity();

		// > Lay ra invoice nhap tu View vao sau do cap nhat so luong va luu lai invoice
		// cu
		Invoice invoice2 = new Invoice();
		invoice2.setProductInfo(invoice.getProductInfo());
		invoice2.setQuantity(invoice.getQuantity() - originQty);
		invoice2.setPrice(invoice.getPrice());

		invoiceDAO.update(invoice);

		// > Luu vao lich su
		historyService.save(invoice, Constant.ACTION_EDIT);

		// > Cap nhat ve so luong, loai hang hoa, ...
		productInStockService.saveOrUpdate(invoice2);
	}

	public List<Invoice> find(String property, Object value) {

		return invoiceDAO.findByProperty(property, value);
	}

	public List<Invoice> getList(Invoice invoice, Paging paging) {

		StringBuilder query = new StringBuilder();
		Map<String, Object> mapParams = new HashMap<>();

		if (invoice != null) {
			if (invoice.getType() != 0) {
				query.append(" and model.type=:type");
				mapParams.put("type", invoice.getType());
			}
			if (!StringUtils.isEmpty(invoice.getCode())) {
				query.append(" and model.code =:code ");
				mapParams.put("code", invoice.getCode());
			}
			if (invoice.getFromDate() != null) {
				query.append(" and model.updateDate >= :fromDate");
				mapParams.put("fromDate", invoice.getFromDate());
			}
			if (invoice.getToDate() != null) {
				query.append(" and model.updateDate <= :toDate");
				mapParams.put("toDate", invoice.getToDate());
			}
		}
		return invoiceDAO.findAll(query.toString(), mapParams, paging);
	}
}
