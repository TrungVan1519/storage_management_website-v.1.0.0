package com.trungvan.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.UserDAO;
import com.trungvan.entity.User;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {
	
	final static Logger log = Logger.getLogger(UserService.class);

	@Autowired
	private UserDAO<User> userDAO;
	
	public List<User> findByProperty(String property, Object value) {
		
		log.info(">> Find by property: " + property + "\tvalue: " + value.toString());
		return userDAO.findByProperty(property, value);
	}
}
