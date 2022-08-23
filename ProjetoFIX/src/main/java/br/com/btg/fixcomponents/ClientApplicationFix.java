package br.com.btg.fixcomponents;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Application;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix42.MessageCracker;

public class ClientApplicationFix implements Application {

	private static final Logger log = LoggerFactory.getLogger(ClientApplicationFix.class);

	private final MessageCracker messageCracker;

	public ClientApplicationFix(MessageCracker messageCracker) {
		this.messageCracker = messageCracker;
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId) {
		log.info("fromAdmin: Message={}, SessionId={}", message, sessionId);
	}

	// resposta para sua sessao vinda do servidor . 
	//Pode ser a execucao da ordem ou cancelamaneto de uma ordem
	@Override
	public void fromApp(Message message, SessionID sessionId) {
		log.info("fromApp: Message={}, SessionId={}", message, sessionId);

		try {
			messageCracker.crack(message, sessionId);
		} catch (UnsupportedMessageType | FieldNotFound | IncorrectTagValue e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void onCreate(SessionID sessionId) {
		log.info("onCreate: SessionId={}", sessionId);
	}

	@Override
	public void onLogon(SessionID sessionId) {
		log.info("onLogon: SessionId={}", sessionId);
	}

	@Override
	public void onLogout(SessionID sessionId) {
		log.info("onLogout: SessionId={}", sessionId);
	}

	
	
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		log.info("toAdmin: Message={}, SessionId={}", message, sessionId);
	}

	// antes de mandar para o servidor(b3) passa aqui
	@Override
	public void toApp(Message message, SessionID sessionId) {
		log.info("toApp: Message={}, SessionId={}", message, sessionId);
	}
}

