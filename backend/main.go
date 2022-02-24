package main

import (
	"context"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
)

func main() {
	initLogging()

	ctx, cancelFunc := signal.NotifyContext(context.Background(), os.Kill, os.Interrupt, syscall.SIGTERM)
	defer cancelFunc()

	log.Info().Msg("Starting up.")
	defer log.Info().Msg("Shutdown.")

	setupRoutes()

	server := &http.Server{Addr: ":9001"}
	doneChan := make(chan struct{})

	// startup server
	go func() {
		log.Info().Str("listenAdress", ":9001").Msg("Start listening.")
		defer log.Info().Msg("Stopped listening.")
		defer close(doneChan)

		if err := server.ListenAndServe(); err != nil {
			log.Error().Err(err).Msg("Cannot listen for requests.")
		}
	}()

	// listen on shutdown signals and gracefully shutdown
	select {
	case <-ctx.Done():
		shutdownCtx, shutdownCancelFunc := context.WithTimeout(context.Background(), 10*time.Second)
		defer shutdownCancelFunc()

		if err := server.Shutdown(shutdownCtx); err != nil {
			log.Fatal().Err(err).Msg("Cannot gracefully shutdown server.")
		}
		<-doneChan
	case <-doneChan:
	}
}

func initLogging() {
	zerolog.SetGlobalLevel(zerolog.DebugLevel) // TODO
	zerolog.TimestampFieldName = "ts"
	zerolog.MessageFieldName = "msg"
}