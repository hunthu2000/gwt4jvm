package $package;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtLoadTest;
import com.mind.gwt.jclient.context.Context;

public class SimpleLoadTest implements EntryPoint {

    @Override
    public void onModuleLoad() {
        Context.getCurrentContext().getClient().success();
    }

    @Test
    public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
        final AtomicLong succeed = new AtomicLong();
        final AtomicLong failure = new AtomicLong();
        int concurrentUsers = Integer.getInteger("concurrentUsers", 1);
        int rampUpSeconds = Integer.getInteger("rampUpSeconds", 0);
        int testDurationSeconds = Integer.getInteger("testDurationSeconds", 0);
        String moduleBaseURL = System.getProperty("moduleBaseURL");
        if (moduleBaseURL == null)
            throw new IllegalArgumentException("No module base URL was specified!");
        new GwtLoadTest(getClass(), moduleBaseURL) {

            @Override
            public void onClientFinished(GwtJavaClient client) {
                if (client.isSucceed())
                    succeed.incrementAndGet();
                else
                    failure.incrementAndGet();
                System.out.println(SimpleLoadTest.class.getSimpleName() + ": concurrent users: " + getConcurrentClients() + ", succeed: " + succeed.get() + ", failure: " + failure.get());
            }

        }.start(concurrentUsers, rampUpSeconds, testDurationSeconds, TimeUnit.SECONDS);
        Assert.assertTrue(true);
    }

}