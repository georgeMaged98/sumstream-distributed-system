package org.example.sumgrpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class SumAppServer {

    private Server server;

    /**
     * Start serving requests.
     */
    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new SumAppService()) // registers the service implementation containing our RPCs.
                .addService(ProtoReflectionService.newInstance()) // Enable reflection
                .build()
                .start();

        System.out.println("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Shutting down gRPC server...");
//            The lambda expression () -> { ... } creates an anonymous inner class that implements the Runnable interface.
//            Inside the lambda, this refers to the instance of the anonymous inner class (the lambda itself), not the instance of the outer class (SumAppServer).
//            To refer to the instance of the outer class (SumAppServer), you need to use SumAppServer.this.
            SumAppServer.this.stop();
            System.err.println("Server shut down.");
        }));

    }

    // Method to stop the server
    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    // Method to block until the server is terminated
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /// This static class is the implementation of the gRPC service defined in the .proto file.
    /// It extends the auto-generated GreeterGrpc.GreeterImplBase class and overrides the methods corresponding
    // to the RPCs (Remote Procedure Calls) defined in the service.
    static class SumAppService extends SumServiceGrpc.SumServiceImplBase {

        @Override
        /// This is a StreamObserver object provided by gRPC to handle the response.
        ///  It allows you to send a response back to the client and signal when the call is complete.
        /// StreamObserver is necessary as it provides async communication, supports different types of RPCs and
        /// offers a graceful way to handle errors during communication using onError().
        public void addTwoNumbers(SumRequest request, StreamObserver<SumReply> responseObserver) {

            // SumReply is the response Object defined in .proto
            // Protobuf uses the builder pattern to construct immutable objects.
            SumReply reply = SumReply.newBuilder().setMessage("Sum: " + (request.getNum1() + request.getNum2())).build();

            // The onNext method is used to send a single response in a unary RPC (one request, one response)
            // In a streaming RPC, this can be called multiple times to send a stream of messages.
            responseObserver.onNext(reply);

            // This signals to the client that the RPC call is complete.
            // After this method is called, no further messages can be sent to the client for this RPC.
            // After calling this, no further calls to onNext or onError are allowed.
            responseObserver.onCompleted();

            System.out.println("DONE!");
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        SumAppServer server = new SumAppServer();
        server.start();
        server.blockUntilShutdown();
    }
}
