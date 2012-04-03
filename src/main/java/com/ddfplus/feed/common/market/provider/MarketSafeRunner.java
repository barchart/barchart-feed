package com.ddfplus.feed.common.market.provider;


interface MarketSafeRunner<Result, Param> {

	Result runSafe(MarketDo market, Param param);

}
