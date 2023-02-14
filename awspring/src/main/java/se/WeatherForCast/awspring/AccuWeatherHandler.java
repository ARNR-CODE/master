package se.WeatherForCast.awspring;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.reactivestreams.Publisher;
import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;
import se.WeatherForCast.awspring.model.ApiConstants;

/**
 * @author: Athmar Mubark  <p>athmar.mubark@yahoo.com<p/>
 * @version: 1.0
 * @since: 23/01-25
 * From my experience never pass json data to handler because error
 * will occur during Lambda deserialization see
 * @Link https://stackoverflow.com/questions/37155595/aws-can-not-deserialize-instance-of-java-lang-string-out-of-start-object
 * second issue if I tried to use supplier interface with SpringBootRequestHandler it will throw an exception
 * from SpringFunctionInitializer line 132 because this is only working with Function interface so when I changed from Supplier
 * to Function it started working as a butter
 */

public class AccuWeatherHandler extends SpringBootRequestHandler<Object, Object> {
    @Override
    public Object handleRequest(Object event, Context context) {
        /*I tried to use only supplier interface, but it doesn't work with SpringBootRequestHandler,
        so I used Function and clear input string because it is not required*/
        Object obj = super.handleRequest(new String(""), context);
        System.out.println("This from object in handleRequest " + obj.toString());
        //Using SNS to send a messages to all subscriptions
        AmazonSNSClient snsClient = (AmazonSNSClient) AmazonSNSClientBuilder.standard().build();
        snsClient.publish(ApiConstants.TOPIC_ARN,obj.toString(),ApiConstants.SUBJECT);
        return obj;
    }
}
//C:\Users\AthmarMu\code\new\awspring\src\main\java\se\WeatherForCast\awspring\AccuWeatherHandler.java