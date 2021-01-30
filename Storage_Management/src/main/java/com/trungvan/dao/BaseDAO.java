package com.trungvan.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.trungvan.entity.Paging;

public interface BaseDAO<E> {
	
	public List<E> findAll(String query, Map<String, Object> mapParams, Paging paging); // > Khi truy van danh sach thi phan trang luon
	public E findById(Class<E> e, Serializable id);
	public List<E> findByProperty(String property , Object value);
	public void save(E instance);
	public void update(E instance);
}
