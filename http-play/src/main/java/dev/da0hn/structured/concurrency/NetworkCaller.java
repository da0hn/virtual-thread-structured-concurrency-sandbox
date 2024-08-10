package dev.da0hn.structured.concurrency;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class NetworkCaller {

  private final String callName;

  public NetworkCaller(String callName) { this.callName = callName; }

  public String makeCall(int seconds) throws IOException, URISyntaxException {

    System.out.println("Begin " + this.callName + " " + Thread.currentThread());
    try {
      URI uri = new URI("http://httpbin.org/delay/" + seconds);

      try (var inputStream = uri.toURL().openStream()) {
        return new String(inputStream.readAllBytes());
      }
    } finally {
      System.out.println("Ending " + this.callName + " " + Thread.currentThread());
    }

  }


}
