package com.syrigmophonia.rpc.provider.impl;

import com.syrigmophonia.rpc.provider.HelloService;

public class HelloServiceImpl implements HelloService {

	@Override
	public String hello(String param) {
		// TODO Auto-generated method stub
		return "自定义参数：" + param;
	}

}
