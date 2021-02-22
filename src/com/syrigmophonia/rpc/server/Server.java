package com.syrigmophonia.rpc.server;

import java.io.IOException;

public interface Server {
	void stop();

	void start() throws IOException;

	void register(Class serviceInterface, Class serviceImpl);

	boolean isRunning();

	int getPort();
}
