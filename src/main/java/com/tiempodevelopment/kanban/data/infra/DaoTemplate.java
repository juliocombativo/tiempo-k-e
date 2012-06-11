package com.tiempodevelopment.kanban.data.infra;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class DaoTemplate<K, I> {
	@PersistenceContext
	EntityManager manager;
	
	public DaoTemplate(EntityManager manager) {
		this.manager = manager;
	}
	
	public void add(K k) {
		manager.persist(k);
	}
	
	@SuppressWarnings("unchecked")
	public List<K> byNamedQuery(String queryName, Object... params) {
		Query query = manager.createNamedQuery(queryName);
		
		if(params != null) 
			for(int i = 0; i < params.length; i++) 
				query.setParameter(i + 1, params[i]);
		
		return query.getResultList();
	}
	
	public K uniqueByNamedQuery(String queryName, Object... params) {
		List<K> ks = byNamedQuery(queryName, params);
		if(ks.size() > 0)
			return ks.get(0);
		return null;
	}

	public void delete(K k) {
		manager.remove(k);
	}

	public K byId(Class<K> klazz, I key) {
		return manager.find(klazz, key);
	}
}
