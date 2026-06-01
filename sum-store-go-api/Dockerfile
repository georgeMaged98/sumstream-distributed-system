# Multi-stage build: compile in a full Go image, run in a minimal Alpine image.
# This keeps the final image small by excluding the Go toolchain and build cache.

FROM golang:1.24-alpine AS builder

WORKDIR /sum-store

COPY go.mod go.sum ./
RUN go mod download

COPY . .

# CGO_ENABLED=0 produces a statically linked binary with no C library dependencies,
# so it runs correctly in the minimal Alpine final stage.
RUN CGO_ENABLED=0 GOOS=linux go build -o sum-store-api .


FROM alpine:3.21

WORKDIR /sum-store-app

COPY --from=builder /sum-store/sum-store-api .

EXPOSE 8080

CMD ["./sum-store-api"]
