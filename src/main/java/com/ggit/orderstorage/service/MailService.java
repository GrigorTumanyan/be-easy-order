package com.ggit.orderstorage.service;

public interface MailService {

	void sendMail(String clientMail, String subject, String message);

}
