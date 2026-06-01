package main

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"os"
	"sum-store-api/routes"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/segmentio/kafka-go"
)

func getEnv(key, defaultVal string) string {
	if val := os.Getenv(key); val != "" {
		return val
	}
	return defaultVal
}

type SumMessage struct {
	Sum int `json:"sum"`
}

func main() {

	broker := getEnv("KAFKA_BOOTSTRAP_SERVERS", "localhost:9092")
	outputFile := getEnv("OUTPUT_FILE", "/tmp/latest-sum.txt")

	kafkaReader := kafka.NewReader(kafka.ReaderConfig{
		Brokers:         []string{broker},
		Topic:           "sum-topic",
		GroupID:         "sum-group",
		StartOffset:     kafka.FirstOffset,
		MinBytes:        1,
		MaxBytes:        10e6,
		ReadLagInterval: -1,
	})

	go func() {
		defer kafkaReader.Close()

		for {
			m, err := kafkaReader.ReadMessage(context.Background())
			if err != nil {
				log.Printf("error reading message: %v", err)
				time.Sleep(time.Second)
				continue
			}

			var msg SumMessage
			if err := json.Unmarshal(m.Value, &msg); err != nil {
				log.Printf("error parsing message: %v", err)
				continue
			}

			current := 0
			if data, err := os.ReadFile(outputFile); err == nil {
				fmt.Sscanf(string(data), "%d", &current)
			}

			total := current + msg.Sum

			if err := os.WriteFile(outputFile, []byte(fmt.Sprintf("%d", total)), 0644); err != nil {
				log.Printf("error writing to file: %v", err)
				continue
			}

			log.Printf("new sum: %d | running total: %d", msg.Sum, total)
		}
	}()

	r := gin.Default()

	routes.SetupRoutes(r)

	r.Run(":8080")
}
