package main

import (
	"context"
	"fmt"
	"github.com/daresaydigital/azure-notificationhubs-go"
	"io"
	"net/http"
)

type user struct {
	id        string // tag
	deviceIds []string
}

func setupRoutes() {
	http.HandleFunc("/register", handleRegister)

	http.HandleFunc("/send", handleSend)
}

func handleRegister(writer http.ResponseWriter, request *http.Request) {

}

func handleSend(writer http.ResponseWriter, request *http.Request) {
	if request.Method != http.MethodPost {
		http.Error(writer, "Request method must be POST", http.StatusBadRequest)
		return
	}

	payload, err := io.ReadAll(request.Body)
	if err != nil {
		http.Error(writer, "Request body could not be read", http.StatusBadRequest)
		return
	}

	hub := notificationhubs.NewNotificationHub("Endpoint=sb://codhack.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=wqry5NCadjHRfX0Jswh5MOBPmJxQgcTS1jb6tSgnsVU=", "cod-hub-01")
	//n, _ := notificationhubs.NewNotification(notificationhubs.GcmFormat, payload)
	n, _ := notificationhubs.NewNotification(notificationhubs.Template, payload)

	// Broadcast push
	b, t, err := hub.Send(context.TODO(), n, nil)
	if err != nil {
		panic(err)
	}

	fmt.Println("Message successfully created:", string(b), t)
}