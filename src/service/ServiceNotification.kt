package com.mindeurfou.service

import com.mindeurfou.utils.NotificationType

open class ServiceNotification {

			// Syncronich<HashSet> de la mort 
	private val observers: MutableList<Int> = mutableListOf()

	fun addObserver(observer: Int){

	} 

	fun notifyObserver(notifType : NotificationType) {

	}

}