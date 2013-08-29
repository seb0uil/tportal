/*
    This file is part of tPortal.

    tPortal is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    tPortal is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with tPortal.  If not, see <http://www.gnu.org/licenses/>.

    The original code was written by Sebastien Bettinger <sebastien.bettinger@gmail.com>

 */

package net.tinyportal.service.visit.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import net.tinyportal.service.visit.model.Visit;

import com.google.appengine.api.datastore.Key;

public class VisitDao {
	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("transactions-optional");
	public static Key save(Visit content) {
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

	public static Visit findByKey(Key key) {
		EntityManager manager = FACTORY.createEntityManager();
		try {
			return manager.find(Visit.class, key);
		} finally {
			manager.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static Visit findByEmail(String email) {
		EntityManager manager = FACTORY.createEntityManager();
		try {
			Query query = manager.createQuery
					("select c from Visit c where c.email like :email");
			query.setParameter("email", email);
			List<Visit> resultList = query.getResultList();
			resultList.size();
			return resultList.get(0);
		} finally {
			manager.close();
		}
	}
}
