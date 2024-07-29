package com.rvlt.ecommerce.mock;

import com.google.gson.Gson;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MockConcurrentOrderSubmit {
  //  static ;
  static String mockUrl = "http://localhost:8080";

  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    System.out.println("Mock MockConcurrentOrderSubmit START");
    for (int i = 0; i < 8; i++) {
      RequestMessage<SubmitOrderRq> rq = new RequestMessage<>();
      Date date = new Date(); // current date and time
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
      String formattedDate = formatter.format(date);
      System.out.println(formattedDate);
      rq.setTime(formattedDate);
      rq.setData(new SubmitOrderRq(String.valueOf(i + 1)));

      CloseableHttpClient httpclient = HttpClients.createDefault();
      HttpPost httpPost = new HttpPost(mockUrl + "/orders/submit");
      httpPost.addHeader("Content-Type", "application/json");
      System.out.println(new Gson().toJson(rq));
      httpPost.setEntity(new StringEntity(new Gson().toJson(rq)));
      CloseableHttpResponse rs = httpclient.execute(httpPost);
      System.out.println("################ START" + (i + 1) + "###############");
      System.out.println("Status: " + rs.getStatusLine().getStatusCode());
      BufferedReader reader = new BufferedReader(new InputStreamReader(
              rs.getEntity().getContent()));

      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = reader.readLine()) != null) {
        response.append(inputLine);
      }
      reader.close();
      // print result
      System.out.println(response);
      httpclient.close();
      System.out.println("################ END" + (i + 1) + "###############");
    }
    System.out.println("Mock MockConcurrentOrderSubmit END");
    long finish = System.currentTimeMillis();
    long timeElapsed = finish - start;
    System.out.println("timeElapsed: " + timeElapsed);
  }
}
