package br.com.btg.util;

import java.util.HashMap;
import java.util.HashSet;

import br.com.btg.vo.OrderSide;
import br.com.btg.vo.OrderTIF;
import br.com.btg.vo.OrderType;
import br.com.btg.vo.TwoWayMap;
import quickfix.SessionID;
import quickfix.field.ExecID;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TimeInForce;

public class CommonsUtils {
	static private final TwoWayMap sideMap = new TwoWayMap();
	static private final TwoWayMap typeMap = new TwoWayMap();
	static private final TwoWayMap tifMap = new TwoWayMap();
	static private final HashMap<SessionID, HashSet<ExecID>> execIDs = new HashMap<>();

	public static Side sideToFIXSide(OrderSide side) {
		return (Side) sideMap.getFirst(side);
	}

	public static OrderSide FIXSideToSide(Side side) {
		return (OrderSide) sideMap.getSecond(side);
	}

	public static OrdType typeToFIXType(OrderType type) {
		return (OrdType) typeMap.getFirst(type);
	}

	public static OrderType FIXTypeToType(OrdType type) {
		return (OrderType) typeMap.getSecond(type);
	}

	public static TimeInForce tifToFIXTif(OrderTIF tif) {
		return (TimeInForce) tifMap.getFirst(tif);
	}

	public static OrderTIF FIXTifToTif(TimeInForce tif) {
		return (OrderTIF) typeMap.getSecond(tif);
	}

	static {
		sideMap.put(OrderSide.BUY, new Side(Side.BUY));
		sideMap.put(OrderSide.SELL, new Side(Side.SELL));
		sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));
		sideMap.put(OrderSide.SHORT_SELL_EXEMPT, new Side(Side.SELL_SHORT_EXEMPT));
		sideMap.put(OrderSide.CROSS, new Side(Side.CROSS));
		sideMap.put(OrderSide.CROSS_SHORT, new Side(Side.CROSS_SHORT));

		typeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
		typeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
		typeMap.put(OrderType.STOP, new OrdType(OrdType.STOP_STOP_LOSS));
		typeMap.put(OrderType.STOP_LIMIT, new OrdType(OrdType.STOP_LIMIT));

		tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
		tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
		tifMap.put(OrderTIF.OPG, new TimeInForce(TimeInForce.AT_THE_OPENING));
		tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
		tifMap.put(OrderTIF.GTX, new TimeInForce(TimeInForce.GOOD_TILL_CROSSING));
	}
	
	
	public static boolean alreadyProcessed(ExecID execID, SessionID sessionID) {
		HashSet<ExecID> set = execIDs.get(sessionID);
		if (set == null) {
			set = new HashSet<>();
			set.add(execID);
			execIDs.put(sessionID, set);
			return false;
		} else {
			if (set.contains(execID))
				return true;
			set.add(execID);
			return false;
		}
	}
}
