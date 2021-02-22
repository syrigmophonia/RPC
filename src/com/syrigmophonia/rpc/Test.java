package com.syrigmophonia.rpc;

import java.io.IOException;

import com.syrigmophonia.rpc.provider.HelloService;
import com.syrigmophonia.rpc.provider.impl.HelloServiceImpl;
import com.syrigmophonia.rpc.server.Server;
import com.syrigmophonia.rpc.server.impl.ServerImpl;

public class Test {
	public static void main(String[] args) throws IOException {
		new Thread(new Runnable() {
			public void run() {
				try {
					Server serviceServer = new ServerImpl(8088);
					serviceServer.register(HelloService.class, HelloServiceImpl.class);
					serviceServer.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
