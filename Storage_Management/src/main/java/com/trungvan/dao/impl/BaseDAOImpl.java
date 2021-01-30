package com.trungvan.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trungvan.dao.BaseDAO;
import com.trungvan.entity.Paging;

@Repository
@Transactional(rollbackFor = Exception.class)
public class BaseDAOImpl<E> implements BaseDAO<E>{
	
	final Logger log = Logger.getLogger(BaseDAOImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<E> findAll(String additionalQueryString, Map<String, Object> mapParams, Paging paging) {
		
		log.info(">> Find all record from db: ");
		
		StringBuilder queryString = new StringBuilder("");
		queryString.append(" from ").append(getGenericName())
									.append(" as model where model.activeFlag = 1");

		StringBuilder countQueryString = new StringBuilder("");
		countQueryString.append(" select count(*) from ").append(getGenericName()) 
														 .append(" as model where model.activeFlag = 1"); 
		
		if(additionalQueryString != null && !additionalQueryString.isEmpty()) {
			
			queryString.append(additionalQueryString);
			countQueryString.append(additionalQueryString);
		}
		
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		Query<E> countQuery = sessionFactory.getCurrentSession().createQuery(countQueryString.toString());
		
		if(mapParams != null && !mapParams.isEmpty()) {
			
			for(String key: mapParams.keySet()) {
				
				query.setParameter(key, mapParams.get(key));
				countQuery.setParameter(key, mapParams.get(key));
			}
		}
		
		if(paging != null) {
			
			query.setFirstResult(paging.getOffset());	// > from model where model.activeFlag = 1 limit 0, 10
			query.setMaxResults(paging.getRecordPerPage());
			long totalRecords = (long) countQuery.uniqueResult();
			paging.setTotalRows(totalRecords);
		}
		
		log.info("> Query find all ==>" + queryString.toString());
		
		return query.getResultList();
	}

	public E findById(Class<E> e, Serializable id) {
		
		log.info(">> Find by ID: ");
		return sessionFactory.getCurrentSession().get(e, id);
	}

	public List<E> findByProperty(String property, Object value) {
		
		log.info(">> Find by property: ");
		
		StringBuilder queryString = new StringBuilder();
		queryString.append(" from ").append(getGenericName())
									.append(" as model where model.activeFlag = 1 and model.")
									.append(property)
									.append(" = ?");
		
		log.info("> Query find by property ==> " + queryString.toString());
		
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		query.setParameter(0, value);
		
		return query.getResultList();
		
	}

	public void save(E instance) {
		
		log.info(">> Save instance: ");
		sessionFactory.getCurrentSession().saveOrUpdate(instance);
	}

	public void update(E instance) {
		
		log.info(">> Update instance: ");
		sessionFactory.getCurrentSession().merge(instance);
	}
	
	// Dung de xac dinh kieu du lieu truyen vao Generic
	public String getGenericName() {

		String generic = "null";
		
		String classNameType = getClass().getGenericSuperclass().toString();
		log.info(">> classNameType: " + classNameType);
		
		// Cach 1: Tra ve "com.trungvan.entity.User"
		Pattern pattern = Pattern.compile("\\<(.*?)\\>");
		Matcher matcher = pattern.matcher(classNameType);
		log.info(">> matcher: " + matcher);
		if(matcher.find()) {
			
			generic = matcher.group(1);
		}
		
//		// Cach 2: Lay han? ra "User" 
//		Pattern pattern = Pattern.compile("<(.*?)>");
//		Matcher matcher = pattern.matcher(classNameType);
//		if(matcher.find()) {
//			
//			String subClassNameType = matcher.group(1);
//			String[] generics = Pattern.compile("\\.").split(subClassNameType);
//			generic = generics[generics.length - 1];
//			
//			log.info("duma:" + generic);
//		}
		
		return generic;
	}
}
