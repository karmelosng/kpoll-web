package com.karmelos.kpoll.action;

import com.karmelos.kpoll.service.PersistenceService;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -1067140797547737901L;

	protected PersistenceService persistenceService;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}

	
	
}
