package com.sumstream.sum.grpc;

import com.sumstream.sum.grpc.proto.SumReply;
import com.sumstream.sum.grpc.proto.SumRequest;
import com.sumstream.sum.grpc.proto.SumServiceGrpc;
import com.sumstream.sum.grpc.services.SumService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;


/// This static class is the implementation of the gRPC service defined in the .proto file.
/// It extends the auto-generated GreeterGrpc.GreeterImplBase class and overrides the methods corresponding
// to the RPCs (Remote Procedure Calls) defined in the service.
@GrpcService // It is similar to:REST: @RestController
public class GrpcSumService extends SumServiceGrpc.SumServiceImplBase {

    private final SumService sumService;

    public GrpcSumService(SumService sumService) {
        this.sumService = sumService;
    }


    @Override
    /// This is a StreamObserver object provided by gRPC to handle the response.
    ///  It allows you to send a response back to the client and signal when the call is complete.
    /// StreamObserver is necessary as it provides async communication, supports different types of RPCs and
    /// offers a graceful way to handle errors during communication using onError().
    public void addTwoNumbers(SumRequest request, StreamObserver<SumReply> responseObserver) {

        try {
            sumService.addTwoNumbers(request);


            // SumReply is the response Object defined in .proto
            // Protobuf uses the builder pattern to construct immutable objects.
            // Prepare rpc reply
            SumReply reply = SumReply.newBuilder().setMessage("Sum: " + (request.getNum1() + request.getNum2())).build();


            // The onNext method is used to send a single response in a unary RPC (one request, one response)
            // In a streaming RPC, this can be called multiple times to send a stream of messages.
            responseObserver.onNext(reply);

            // This signals to the client that the RPC call is complete.
            // After this method is called, no further messages can be sent to the client for this RPC.
            // After calling this, no further calls to onNext or onError are allowed.
            responseObserver.onCompleted();

            System.out.println("Adding Two Numbers Request Processed Completely!!");
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("Failed to process request" + e.getMessage())
//                            .withCause(e)
                            .asRuntimeException()
            );
        }

        // Send Msg to Kafka
//            sumKafkaProducer.sendMessageToTopic./mvnw spring-boot:run();
//            System.out.println("SEND MSG!");

    }
}
