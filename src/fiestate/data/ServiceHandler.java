package fiestate.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Entity;
import android.util.Log;

public class ServiceHandler 
{
	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;
	
	public ServiceHandler()
	{
	}	

	public String makeServiceCall(String url, int method)
	{
		return this.makeServiceCall(url, method, null);
	}
	
	public String makeServiceCall(String url, int method, List<NameValuePair> params)
	{
		try
		{
			//HTTP Cliente
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			//Comprueba el tipo de método de la petición HTTP
			if(method == POST)
			{
				HttpPost httpPost = new HttpPost(url);
				
				//Añadimos parámetros de la petición POST
				if (params != null)
				{
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}
				
				httpResponse = httpClient.execute(httpPost);
			}
			else if(method == GET)
			{
				if(params != null)
				{
					String paramString = URLEncodedUtils.format(params, "utf-8");
					url += "?" + paramString;
				}
				
				HttpGet httpGet = new HttpGet(url);
				
				httpResponse = httpClient.execute(httpGet);
				
				Log.i("SERVICEHANDLER", "RESPUESTA HTTP -> "+httpResponse);
			}
			
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch(ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return response;
	}
	
}























