package com.syrigmophonia.rpc.server.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.syrigmophonia.rpc.server.Server;

public class ServerImpl implements Server {

	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private static final HashMap<String, Class> serviceRegistry = new HashMap<>();

	private static boolean isRunning = false;

	private static int port;

	public ServerImpl(int port) {
		this.port = port;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		isRunning = false;
		executor.shutdown();
	}

	@Override
	public void start() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(port));
		System.out.println("start server");
		try {
			while (true) {
				// 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
				executor.execute(new ServiceTask(serverSocket.accept()));
			}
		} finally {
			serverSocket.close();
		}

	}

	@Override
	public void register(Class serviceInterface, Class serviceImpl) {
		// TODO Auto-generated method stub
		serviceRegistry.put(serviceInterface.getName(), serviceImpl);
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return isRunning;
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return port;
	}

	private static class ServiceTask implements Runnable {
		Socket clientSocket = null;

		public ServiceTask(Socket clientSocket) {
			// TODO Auto-generated constructor stub
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ObjectInputStream inputStream = null;
			ObjectOutputStream outputStream = null;
			try {
				// 2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
				inputStream = new ObjectInputStream(clientSocket.getInputStream());
				String serviceName = inputStream.readUTF();
				String methodName = inputStream.readUTF();
				Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
				Class serviceClass = serviceRegistry.get(serviceName);
				Object[] arguments = (Object[]) inputStream.readObject();
				if (null == serviceClass) {
					throw new ClassNotFoundException(serviceName + " not found");
				}
				Method method = serviceClass.getMethod(methodName, parameterTypes);
				Object result = method.invoke(serviceClass.newInstance(), arguments);
				// 3.将执行结果反序列化，通过socket发送给客户端
				outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
				outputStream.writeObject(result);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				if (clientSocket != null) {
					try {
						clientSocket.close();
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
			}
		}

	}

}
