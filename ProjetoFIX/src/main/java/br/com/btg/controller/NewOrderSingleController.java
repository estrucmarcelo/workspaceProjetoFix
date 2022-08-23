package br.com.btg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.btg.ProjetoFixApplication;
import br.com.btg.service.NewOrderSingleService;
import br.com.btg.vo.Order;
import br.com.btg.vo.OrderSide;
import br.com.btg.vo.OrderTIF;
import br.com.btg.vo.OrderType;
import lombok.AllArgsConstructor;
import quickfix.Initiator;
import quickfix.SessionID;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
public class NewOrderSingleController {

	
	private final Initiator clientInitiator;
	
	
	@Autowired
	private NewOrderSingleService service;
	
	
	@PostMapping("/buy-order")
	@ResponseStatus(OK)
	private void createBuyOrder(@RequestParam String symbol, 
			@RequestParam int quantity,@RequestParam String orderType, 
			@RequestParam double limitPrice,@RequestParam double stopPrice) {
		
		Order order = new Order();
		order.setSide(OrderSide.BUY);
		order.setType(OrderType.parse(orderType));
		order.setTIF(OrderTIF.DAY);
		order.setSymbol(symbol);
		order.setQuantity(quantity);
		order.setOpen(quantity);
		
		OrderType type = order.getType();
		if(type == OrderType.LIMIT || type == OrderType.STOP_LIMIT)
			order.setLimit(limitPrice);
		if(type == OrderType.STOP || type == OrderType.STOP_LIMIT)
			order.setStop(stopPrice);
		
		
		SessionID sessionID = clientInitiator.getSessions().stream().
				findFirst().orElseThrow(RuntimeException::new);
		
		order.setSessionID(sessionID);
		
		service.send42(order);
		
	}
	
	@PostMapping("/cancel-order")
	@ResponseStatus(OK)
	private void createCancelOrder(@RequestParam String orderId) {
		Order order = ProjetoFixApplication.orderMapId.get(orderId);
		SessionID sessionID = clientInitiator.getSessions().stream().
				findFirst().orElseThrow(RuntimeException::new);
		order.setSessionID(sessionID);
		service.cancel42(order);
		
	}
	
	
}
