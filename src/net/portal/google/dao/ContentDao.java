package net.portal.google.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import net.portal.google.model.Content;

import com.google.appengine.api.datastore.Key;
import javax.persistence.Query;

public class ContentDao {
	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("transactions-optional");
	public static Key save(Content content) {
		EntityManager manager = FACTORY.createEntityManager();
		EntityTransaction tx = manager.getTransaction();
		try {
			tx.begin();
			manager.persist(content);
			tx.commit();
			return content.getKey();
		} finally {
			manager.close();
		}
	}

	public static Content findByKey(Key key) {
		EntityManager manager = FACTORY.createEntityManager();
		try {
			return manager.find(Content.class, key);
		} finally {
			manager.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static Content findByTitle(String title) {
		EntityManager manager = FACTORY.createEntityManager();
		try {
			Query query = manager.createQuery
					("select c from Content c where c.title like :title");
			query.setParameter("title", title);
			List<Content> resultList = query.getResultList();
			resultList.size();
			return resultList.get(0);
		} finally {
			manager.close();
		}
	}
}
