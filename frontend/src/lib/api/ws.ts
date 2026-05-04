export class WebSocketClient {
  private ws: WebSocket | null = null;
  private url: string;
  private maxReconnectRetries = 5;
  private reconnectCount = 0;
  private listeners: Set<(data: any) => void> = new Set();

  constructor(url: string) {
    this.url = url;
  }

  connect() {
    if (this.ws?.readyState === WebSocket.OPEN) return;

    this.ws = new WebSocket(this.url);

    this.ws.onopen = () => {
      this.reconnectCount = 0;
      console.log(`[WS] Connected to ${this.url}`);
    };

    this.ws.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        this.listeners.forEach((listener) => listener(data));
      } catch (e) {
        console.error('[WS] Failed to parse message', e);
      }
    };

    this.ws.onclose = () => {
      if (this.reconnectCount < this.maxReconnectRetries) {
        setTimeout(() => {
          this.reconnectCount++;
          this.connect();
        }, Math.pow(2, this.reconnectCount) * 1000); // Exponential backoff
      }
    };
  }

  subscribe(callback: (data: any) => void) {
    this.listeners.add(callback);
    return () => this.listeners.delete(callback);
  }

  send(event: string, payload: any) {
    if (this.ws?.readyState === WebSocket.OPEN) {
      this.ws.send(JSON.stringify({ event, payload }));
    }
  }

  disconnect() {
    this.ws?.close();
    this.ws = null;
  }
}

// Global instance for app-wide notifications
export const notificationWS = new WebSocketClient(process.env.NEXT_PUBLIC_WS_URL || 'ws://localhost:8080/ws');

