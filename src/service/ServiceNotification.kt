package com.mindeurfou.service

import com.mindeurfou.utils.NotificationType

open class ServiceNotification {

			// Syncronich<HashSet> de la mort 
	private val observers: MutableList<Int> = mutableListOf()

	protected fun addObserver(observer: Int){

	} 

	protected fun notifyObserver(notifType : NotificationType) {

	}

}