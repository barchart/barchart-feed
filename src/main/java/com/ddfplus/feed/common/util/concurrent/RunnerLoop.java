package com.ddfplus.feed.common.util.concurrent;

import java.util.List;

public interface RunnerLoop<Param> {

	<Result> void runLoop(Runner<Result, Param> task, List<Result> list);

}
