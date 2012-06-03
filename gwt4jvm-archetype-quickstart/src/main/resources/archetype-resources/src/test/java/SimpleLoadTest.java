package $package;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.mind.gwt.jclient.GwtJavaClient;
import com.mind.gwt.jclient.GwtLoadTest;
import com.mind.gwt.jclient.context.Context;

public class SimpleLoadTest implements EntryPoint {

    private static final int CONCURRENT_USERS = Integer.getInteger("concurrentUsers", 1);
    private static final int RAMP_UP_SECONDS = Integer.getInteger("rampUpSeconds", 0);
    private static final int TEST_DURATION_SECONDS = Integer.getInteger("testDurationSeconds", 0);
    private static final String MODULE_BASE_URL = System.getProperty("moduleBaseURL");

    @Override
    public void onModuleLoad() {
        Context.getCurrentContext().getClient().success();
    }

    @Test
    public void test() throws InstantiationException, IllegalAccessException, InterruptedException {
        final AtomicLong succeed = new AtomicLong();
        final AtomicLong failure = new AtomicLong();
        if (MODULE_BASE_URL == null) {
            throw new IllegalArgumentException("No module base URL was specified, use 'moduleBaseURL' system property to specify it!");
        }
        new GwtLoadTest(getClass(), MODULE_BASE_URL) {

            @Override
            public void onClientFinished(GwtJavaClient client) {
                if (client.isSucceed())
                    succeed.incrementAndGet();
                else
                    failure.incrementAndGet();
                System.out.println(SimpleLoadTest.class.getSimpleName() + ": concurrent users: " + getConcurrentClients() + ", succeed: " + succeed.get() + ", failed: " + failure.get());
            }

        }.start(CONCURRENT_USERS, RAMP_UP_SECONDS, TEST_DURATION_SECONDS, TimeUnit.SECONDS);
        Assert.assertTrue(true);
    }

}