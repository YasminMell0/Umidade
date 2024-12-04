#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <DHT.h>
// Configurações de Wi-Fi
const char* ssid = "Yas";
const char* password = "Yas1406#";
// Endereço da API do backend
const char* serverUrl = "http://192.168.99.73:3031/add"; // Substitua pelo IP ou URL do backend
// Configuração do sensor DHT
#define DHTPIN D4 // Pino onde o DHT11 está conectado
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);
WiFiClient wifiClient; // Adiciona o objeto WiFiClient
void setup() {
  Serial.begin(115200);
  dht.begin();
  // Conexão com Wi-Fi
  WiFi.begin(ssid, password);
  Serial.print("Conectando ao Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("\nConectado ao Wi-Fi!");
}
void loop() {
  if (WiFi.status() == WL_CONNECTED) { // Verifica se está conectado ao Wi-Fi
    float humidity = dht.readHumidity();
    float temperature = dht.readTemperature();
    // Verifica se os valores do sensor são válidos
    if (isnan(humidity) || isnan(temperature)) {
      Serial.println("Falha ao ler do sensor DHT!");
      delay(2000);
      return;
    }
    // Cria o JSON para enviar (sem incluir dateTime)
    String postData = "{\"temperature\":" + String(temperature) +
                      ",\"humidity\":" + String(humidity) + "}";
    // Envia os dados via HTTP POST
    HTTPClient http;
    http.begin(wifiClient, serverUrl); // Use o WiFiClient no begin
    http.addHeader("Content-Type", "application/json");
    int httpResponseCode = http.POST(postData);
    if (httpResponseCode > 0) {
      Serial.println("Dados enviados com sucesso!");
      Serial.print("Resposta do servidor: ");
      Serial.println(httpResponseCode);
    } else {
      Serial.print("Erro ao enviar dados: ");
      Serial.println(httpResponseCode);
    }
    http.end();
  } else {
    Serial.println("Wi-Fi desconectado. Tentando reconectar...");
    WiFi.begin(ssid, password);
  }
  delay(60000); // Aguarda 1 minuto antes de enviar novamente
}