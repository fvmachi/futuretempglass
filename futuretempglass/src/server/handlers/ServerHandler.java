package server.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import server.Server;
import server.Session;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class ServerHandler implements HttpHandler{

	@Override
	public void handle(HttpExchange ex) throws IOException
	{
		try
		{
			ex.setAttribute("session", getActiveSession(ex));
			if(!authenticate(ex))
			{
				sendHeader(ex);
				sendResponse("MUST LOGIN", ex);
				finish(ex);
				return;
			}
			if("GET".equals(ex.getRequestMethod()))
			{
				onGet(ex);
			}
			else if("POST".equals(ex.getRequestMethod()))
			{
				onPost(ex);
			}
			else if("PUT".equals(ex.getRequestMethod()))
			{
				onPut(ex);
			}
		}
		catch(Exception e)
		{
			sendResponse(e.getMessage(), ex);
		}
		finally
		{
			finish(ex);
		}
	}

	private static Session getActiveSession(HttpExchange ex)
	{
		List<Session> activeSessions = Server.getActiveSessions();
		for(Session session: activeSessions)
		{
			if(session.isMatchingSession(ex))
			{
				return session;
			}
		}
		return null;
	}

	protected void finish(HttpExchange ex) throws IOException
	{
		ex.getRequestBody().close();
		ex.getResponseBody().flush();
		ex.getResponseBody().close();
		ex.close();
	}

	protected void onGet(HttpExchange ex) throws Exception
	{

	}

	protected void onPost(HttpExchange ex) throws Exception
	{

	}

	protected void onPut(HttpExchange ex) throws Exception
	{

	}

	protected boolean authenticate(HttpExchange ex)
	{
		return ex.getAttribute("session") != null;
	}

	protected void sendHeader(HttpExchange ex) throws IOException
	{
		ex.sendResponseHeaders(200, 0);
	}

	protected void sendResponse(String response, HttpExchange ex)
			throws IOException
	{
		ex.getResponseBody().write(response.getBytes());
	}

	protected void sendResponse(Object object, HttpExchange ex)
			throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		sendResponse(mapper.writeValueAsString(object), ex);
	}

	protected String getRequestData(HttpExchange ex) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				ex.getRequestBody()));
		StringBuilder builder = new StringBuilder();
		String line = reader.readLine();
		while (line != null)
		{
			builder.append(line);
			line = reader.readLine();
		}
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	protected HashMap<String, String> getParameters(HttpExchange ex)
	{
		HashMap<String, String> parameters = new HashMap<String, String>();
		if(ex.getRequestURI().getQuery() != null)
		{
			String[] queries = ex.getRequestURI().getQuery().split("&");
			for(String query: queries)
			{
				String[] pair = query.split("=");
				if(pair.length == 1)
				{
					parameters.put(pair[0], "");
				}
				else
				{
					parameters.put(pair[0], pair[1]);
				}
			}
		}
		ex.setAttribute("parameters", parameters);
		return (HashMap<String, String>)ex.getAttribute("parameters");
	}

	protected <T> T getObjectFromRequestBody(Class<T> clazz, HttpExchange ex)
			throws IOException
	{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(getRequestData(ex), clazz);
	}

}
