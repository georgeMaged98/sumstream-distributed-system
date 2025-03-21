package org.example.sumgrpc;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SumServiceTest {

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule(); // To automatically cleanup resources

    private SumServiceGrpc.SumServiceBlockingStub sumServiceBlockingStub;

    @BeforeEach
    public void setUp() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        // This creates a server and registers it for automatic cleanup
        grpcCleanup.register(InProcessServerBuilder.forName(serverName)
                .directExecutor()
                .addService(new SumAppServer.SumAppService())
                .build()
                .start());

        // This creates a client and registers it for automatic cleanup
        sumServiceBlockingStub = SumServiceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName)
                        .directExecutor()
                        .build()));
    }

    @Test
    public void testAddTwoNumbersSuccessTest() {

        SumRequest sumRequest = SumRequest.newBuilder()
                .setNum1(3)
                .setNum2(5)
                .build();

        SumReply sumReply = sumServiceBlockingStub.addTwoNumbers(sumRequest);

        assertEquals(sumReply.getMessage(), "Sum: 8");
    }


    @Test
    public void testAddTwoNumbersFailTest() {

        SumRequest sumRequest = SumRequest.newBuilder()
                .setNum1(3)
                .setNum2(4)
                .build();

        SumReply sumReply = sumServiceBlockingStub.addTwoNumbers(sumRequest);

        assertNotEquals(sumReply.getMessage(), "Sum: 8");
    }

}
