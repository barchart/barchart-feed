package com.ddfplus.market.impl.mock;

public interface MockMessageVisitor<Result, Param> {

	Result visit(MockMsgTrade message, Param param);

	Result visit(MockMsgBook message, Param param);

}
