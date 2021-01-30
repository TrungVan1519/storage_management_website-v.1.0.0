package com.trungvan.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trungvan.entity.History;
import com.trungvan.entity.Paging;
import com.trungvan.service.HistoryService;
import com.trungvan.utils.Constant;

@Controller
@RequestMapping("/history")
public class HistoryController {

	static final Logger log = Logger.getLogger(HistoryController.class);
	
	@Autowired
	private HistoryService historyService;
	
	@GetMapping(value = {"/list","/list/"})
	public String redirect() {
		return "redirect:/history/list/1";
	}
	
	@RequestMapping("/list/{page}")
	public String list(Model model,
			@ModelAttribute("searchForm") History history,
			@PathVariable("page") int page) {
		
		Map<String,String> mapType = new HashMap<>();
		mapType.put(String.valueOf(Constant.TYPE_ALL), "All");
		mapType.put(String.valueOf(Constant.TYPE_GOODS_RECEIPT), "Goods Receipt");
		mapType.put(String.valueOf(Constant.TYPE_GOODS_ISSUES), "Goods Issues");
		model.addAttribute("mapType", mapType);

		Paging paging = new Paging(5);
		paging.setIndexPage(page);	// > Lay ra page hien tai de tinh offset
		
		model.addAttribute("histories", historyService.findAll(history, paging));
		model.addAttribute("pageInfo", paging);
		
		return "historyView.definition";
	}
}
