package routes

import (
	"github.com/gin-gonic/gin"

	"sum-store-api/handlers"
)

func SetupRoutes(r *gin.Engine) {

	r.GET("/sum", handlers.GetLatestSum)
}
