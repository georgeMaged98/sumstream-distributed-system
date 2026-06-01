package handlers

import (
	"fmt"
	"net/http"
	"os"

	"github.com/gin-gonic/gin"
)

func GetLatestSum(c *gin.Context) {
	outputFile := os.Getenv("OUTPUT_FILE")
	if outputFile == "" {
		outputFile = "/tmp/latest-sum.txt"
	}

	data, err := os.ReadFile(outputFile)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"sum": "NaN"})
		return
	}

	total := 0
	fmt.Sscanf(string(data), "%d", &total)

	c.JSON(http.StatusOK, gin.H{"sum": total})
}
