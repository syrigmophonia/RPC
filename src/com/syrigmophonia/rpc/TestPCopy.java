package com.syrigmophonia.rpc;

import java.net.InetSocketAddress;

import com.syrigmophonia.rpc.client.RPCClient;
import com.syrigmophonia.rpc.provider.HelloService;

public class TestPCopy {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HelloService service = RPCClient.getRemoteProxyObj(HelloService.class,
				new InetSocketAddress("localhost", 8088));
		System.out.println(service.hello("syrigmophonia"));
	}

}
