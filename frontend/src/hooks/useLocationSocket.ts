"use client"

import { useEffect, useRef, useState, useCallback } from "react"
import { io, Socket } from "socket.io-client"

interface LocationUpdate {
  userId: string
  sosId: string
  lat: number
  lng: number
  timestamp: string
  accuracy?: number
}

interface UseLocationSocketOptions {
  sosId?: string
  userId?: string
  enabled?: boolean
  onLocationUpdate?: (update: LocationUpdate) => void
  onConnect?: () => void
  onDisconnect?: () => void
}

interface UseLocationSocketReturn {
  isConnected: boolean
  lastUpdate: LocationUpdate | null
  emitLocation: (lat: number, lng: number, accuracy?: number) => void
  disconnect: () => void
  connect: () => void
}

const SOCKET_URL = process.env.NEXT_PUBLIC_WS_URL || "http://localhost:8084"

export function useLocationSocket({
  sosId,
  userId,
  enabled = true,
  onLocationUpdate,
  onConnect,
  onDisconnect,
}: UseLocationSocketOptions = {}): UseLocationSocketReturn {
  const [isConnected, setIsConnected] = useState(false)
  const [lastUpdate, setLastUpdate] = useState<LocationUpdate | null>(null)
  const socketRef = useRef<Socket | null>(null)

  const connect = useCallback(() => {
    if (socketRef.current?.connected) return

    const socket = io(SOCKET_URL, {
      path: "/ws",
      transports: ["websocket"],
      autoConnect: true,
    })

    socket.on("connect", () => {
      setIsConnected(true)
      onConnect?.()

      if (sosId) {
        socket.emit("join:sos", sosId)
      }
      if (userId) {
        socket.emit("join:user", userId)
      }
    })

    socket.on("disconnect", () => {
      setIsConnected(false)
      onDisconnect?.()
    })

    socket.on("location:update", (data: LocationUpdate) => {
      setLastUpdate(data)
      onLocationUpdate?.(data)
    })

    socketRef.current = socket
  }, [sosId, userId, onConnect, onDisconnect, onLocationUpdate])

  const disconnect = useCallback(() => {
    if (socketRef.current) {
      socketRef.current.disconnect()
      socketRef.current = null
      setIsConnected(false)
    }
  }, [])

  const emitLocation = useCallback(
    (lat: number, lng: number, accuracy?: number) => {
      if (socketRef.current?.connected && sosId && userId) {
        socketRef.current.emit("location:ping", {
          sosId,
          userId,
          lat,
          lng,
          accuracy,
          timestamp: new Date().toISOString(),
        })
      }
    },
    [sosId, userId]
  )

  useEffect(() => {
    if (enabled) {
      connect()
    } else {
      disconnect()
    }

    return () => {
      disconnect()
    }
  }, [enabled, connect, disconnect])

  useEffect(() => {
    if (socketRef.current?.connected && sosId) {
      socketRef.current.emit("join:sos", sosId)
    }
  }, [sosId])

  return {
    isConnected,
    lastUpdate,
    emitLocation,
    disconnect,
    connect,
  }
}

export type { LocationUpdate }