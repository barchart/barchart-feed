package com.ddfplus.feed.common.util.concurrent;

public interface Runner<Result, Param> {

	Result run(Param param);

}
